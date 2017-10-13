package jp.co.docomo.sdk.hitoe.sample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import jp.co.docomo.sdk.hitoe.sample.data.ACCData;
import jp.co.docomo.sdk.hitoe.sample.data.ECGData;
import jp.co.docomo.sdk.hitoe.sample.data.ExData;
import jp.co.docomo.sdk.hitoe.sample.service.ServiceManageInfo;
import jp.ne.docomo.smt.dev.hitoetransmitter.HitoeSdkAPI;
import jp.ne.docomo.smt.dev.hitoetransmitter.sdk.HitoeSdkAPIImpl;

/**
 * Created by Kooriyama on 2016/02/17.
 */
public class APIService extends Service{
    private final String LOG_TAG = this.getClass().getSimpleName();

    private boolean mCurrentMeasure;

    // SDK
    private HitoeSdkAPI mHitoeSdkAPI = null;
    private IAPIServiceCallback mAPIServiceCallback = null;

    // 管理情報
    private ServiceManageInfo mServiceManageInfo = null;

    // ファイルロガー
    private DataLogger mDataLogger = null;

    // 姿勢推定のための一時保存データ
    private ArrayList<String> mListForPosture;
    // 姿勢推定のためのロック
    private ReentrantLock mLockForPosture;
    // 歩行状態推定のための一時保存データ
    private ArrayList<String> mListForWalk;
    // 歩行状態推定のためのロック
    private ReentrantLock mLockForWalk;
    // 左右バランス推定のための一時保存データ
    private ArrayList<String> mListForLRBalance;
    // 左右バランス推定のためのロック
    private ReentrantLock mLockForLRBalance;
    // バッテリデータ
    private String mStrForBattery;

    // 拡張分析のための保存データ
    private ArrayList<ExData>mListForEx;
    // 拡張分析のためのロック
    private ReentrantLock mLockForEx;
    // 拡張分析中フラグ
    private boolean mFlagForEx;

    // DeepSleep対応
    private Context mContext = null;
    private PowerManager.WakeLock mWakeLock = null;
    private final Object mLockObject = new Object();

    /**
     * APIコールバック
     */
    HitoeSdkAPI.APICallback mAPICallback = new HitoeSdkAPI.APICallback() {

        @Override
        public void onResponse(int api_id, int response_id, String responseString) {

//            Log.d(LOG_TAG, "State..onResponse:apiId=" + String.valueOf(api_id) + ",responseId=" + String.valueOf(response_id) + ",resonseObject=" + responseString.replace(CommonConsts.BR, CommonConsts.VB));

            // 応答をログ出力
            mDataLogger.setResponseStr(api_id, response_id, responseString.replace(CommonConsts.BR, CommonConsts.VB));

            switch (api_id) {
                case CommonConsts.API_ID_ADD_RECIVER:

                    // 拡張分析の戻りの場合はフラグを落とす
                    if(responseString.startsWith(CommonConsts.RAW_CONNECTION_PREFFIX)) {

                        // Rawデータの応答であればconnectionIdを保存
                        mServiceManageInfo.setRawConnectionId(responseString);
                    } else if(responseString.startsWith(CommonConsts.BA_CONNECTION_PREFFIX)) {

                        // Baデータの応答であればconnectionIdを保存
                        mServiceManageInfo.setBaConnectionId(responseString);
                    } else if(responseString.startsWith(CommonConsts.EX_CONNECTION_PREFFIX)) {
                        try{

                            mLockForEx.lock();
                            mFlagForEx = false;
                        }finally {

                            mLockForEx.unlock();
                        }
                    }
                        break;
                case CommonConsts.API_ID_CONNECT:

                    if (response_id == CommonConsts.RES_ID_SENSOR_DISCONECT_NOTICE) {
                        // 切断の場合もフラグを落とす
                        try{

                            mLockForEx.lock();

                            mListForEx.clear();
                            mFlagForEx = false;
                        }finally {

                            mLockForEx.unlock();
                        }

                        mCurrentMeasure = false;
                    }else if (response_id == CommonConsts.RES_ID_SENSOR_CONNECT) {
                        // 接続で来たらSessionIdを保存する
                        mServiceManageInfo.setSessionId(responseString);
                        mCurrentMeasure = true;
                    }

                    break;
                case CommonConsts.API_ID_DISCONNECT:

                    // 切断の場合もフラグを落とす
                    try{

                        mLockForEx.lock();

                        mListForEx.clear();
                        mFlagForEx = false;
                    }finally {

                        mLockForEx.unlock();
                    }

                    mCurrentMeasure = false;

                    break;

            }
            if(mAPIServiceCallback == null) {

                // コールバックが設定されていなければ返さない
                return;
            }

            try {
                mAPIServiceCallback.onResponse(api_id, response_id, responseString);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * データレシーバ
     */
    HitoeSdkAPI.DataReceiverCallback mDataReceiverCallback = new HitoeSdkAPI.DataReceiverCallback() {

        @Override
        public void onDataReceive(String connectionId, int response_id, String dataKey, String rawData) {

            Log.d(LOG_TAG, "State..onDataReceive:connectId=" + connectionId + ",dataKey=" + dataKey + ",response_id="+response_id+",rawData=" + rawData.replace("\n", ","));

            // ログ出力
            mDataLogger.setDataStr(dataKey, rawData);

            // データを解析して対象であれば拡張分析を実行する
            if (dataKey.equals("raw.ecg")) {
                parseECGStr(rawData);

            } else if (dataKey.equals("raw.acc")) {
                parseACCStr(rawData);

            } else if (dataKey.equals("raw.rri")) {

            } else if (dataKey.equals("raw.bat")) {

            } else if (dataKey.equals("raw.hr")) {

            } else if (dataKey.equals("raw.saved_hr")) {

            } else if (dataKey.equals("raw.saved_rri")) {

            } else if (dataKey.equals("ba.extracted_rri")) {

            } else if (dataKey.equals("ba.cleaned_rri")) {

            } else if (dataKey.equals("ba.interpolated_rri")) {

            } else if (dataKey.equals("ba.freq_domain")) {
                parseFreqDomain(rawData);

            } else if (dataKey.equals("ba.time_domain")) {

            } else if (dataKey.equals("ex.stress")) {

            } else if (dataKey.equals("ex.posture")) {

            } else if (dataKey.equals("ex.walk")) {

            } else if (dataKey.equals("ex.lr_balance")) {

            }

            // 拡張があれば実行
            // 既に実行中であれば実行しない
            ExData exData = null;
            try{

                mLockForEx.lock();
                if(!mFlagForEx && mListForEx.size() > 0) {
                    mFlagForEx = true;

                    exData = mListForEx.get(0);
                    mListForEx.remove(0);
                }
            }finally {

                mLockForEx.unlock();
            }
            if(exData != null) {

                // nullでなければ拡張解析に投げる
                addExReceiver(exData);
            }

            if(mAPIServiceCallback != null) {

                // コールバックが設定されていれば返す
                try {
                    mAPIServiceCallback.onDataReceive(connectionId, response_id, dataKey, rawData);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                return;
            }
        }

        /**
         * ECGデータをパースする
         * @param data ECGデータ
         */
        private void parseECGStr(String data) {

            String[] lineList=data.split(CommonConsts.BR);
            ECGData ecgData = new ECGData();
            for(int i=0; i < lineList.length; i++) {

                ecgData.setData(lineList[i]);
            }

            return;
        }
        /**
         * ACCデータをパースする
         * @param data ACCデータ
         */
        private void parseACCStr(String data) {

            String[] lineList=data.split(CommonConsts.BR);
            ACCData accData = new ACCData();
            ArrayList<String> postureInputList  = new ArrayList<String>();
            ArrayList<String> walkInputList  = new ArrayList<String>();
            ArrayList<String> lrBalanceInputList  = new ArrayList<String>();

            ArrayList<String> workList = new ArrayList<String>();

            for(int i=0; i < lineList.length; i++) {
                accData.setData(lineList[i]);

                if(mServiceManageInfo.validExKey("ex.posture")) {
                    try {
                        mLockForPosture.lock();
                        mListForPosture.add(lineList[i]);
                        if (mListForPosture.size() > CommonConsts.EX_POSTURE_UNIT_NUM + 5) {

                            for (int j = 0; j < CommonConsts.EX_POSTURE_UNIT_NUM; j++) {

                                postureInputList.add(mListForPosture.get(j));
                            }
                            // 少し多めに追加
                            for (int j = CommonConsts.EX_POSTURE_UNIT_NUM; j < CommonConsts.EX_POSTURE_UNIT_NUM + 5; j++) {

                                postureInputList.add(mListForPosture.get(j));
                            }

                            // 1秒分を削除
                            workList = new ArrayList<String>();
                            for (int j = 25; j < mListForPosture.size(); j++) {

                                workList.add(mListForPosture.get(j));
                            }
                            mListForPosture = workList;
                        }
                    } finally {

                        mLockForPosture.unlock();
                    }
                     if(postureInputList.size() > 0) {

                        try {
                            mLockForEx.lock();
                            mListForEx.add(new ExData("ex.posture", CommonConsts.getExParam("ex.posture"), postureInputList));
                        } finally {
                            mLockForEx.unlock();
                        }

                        postureInputList.clear();
                    }
                }
                if(mServiceManageInfo.validExKey("ex.walk")) {
                    try {
                        mLockForWalk.lock();
                        mListForWalk.add(lineList[i]);
                        if (mListForWalk.size() > CommonConsts.EX_WALK_UNIT_NUM + 5) {

                            for (int j = 0; j < CommonConsts.EX_WALK_UNIT_NUM; j++) {

                                walkInputList.add(mListForWalk.get(j));
                            }
                            // 少し多めに追加
                            for (int j = CommonConsts.EX_WALK_UNIT_NUM; j < CommonConsts.EX_WALK_UNIT_NUM + 5; j++) {

                                walkInputList.add(mListForWalk.get(j));
                            }

                            // 1秒分を削除
                            workList = new ArrayList<String>();
                            for (int j = 25; j < mListForWalk.size(); j++) {

                                workList.add(mListForWalk.get(j));
                            }
                            mListForWalk = workList;
                        }
                    } finally {

                        mLockForWalk.unlock();
                    }
                    if(walkInputList.size() > 0) {

                        try {
                            mLockForEx.lock();
                            mListForEx.add(new ExData("ex.walk", CommonConsts.getExParam("ex.walk"), walkInputList));
                        } finally {
                            mLockForEx.unlock();
                        }

                        walkInputList.clear();
                    }
                }
                if(mServiceManageInfo.validExKey("ex.lr_balance")) {
                    try {
                        mLockForLRBalance.lock();
                        mListForLRBalance.add(lineList[i]);
                        if (mListForLRBalance.size() > CommonConsts.EX_LR_BALANCE_UNIT_NUM + 5) {

                            for (int j = 0; j < CommonConsts.EX_LR_BALANCE_UNIT_NUM; j++) {

                                lrBalanceInputList.add(mListForLRBalance.get(j));
                            }
                            // 少し多めに追加
                            for (int j = CommonConsts.EX_LR_BALANCE_UNIT_NUM; j < CommonConsts.EX_LR_BALANCE_UNIT_NUM + 5; j++) {

                                lrBalanceInputList.add(mListForLRBalance.get(j));
                            }

                            // 1秒分を削除
                            workList = new ArrayList<String>();
                            for (int j = 25; j < mListForLRBalance.size(); j++) {

                                workList.add(mListForLRBalance.get(j));
                            }
                            mListForLRBalance = workList;

                        }
                    } finally {

                        mLockForLRBalance.unlock();
                    }
                    if(lrBalanceInputList.size() > 0) {

                        try {
                            mLockForEx.lock();
                            mListForEx.add(new ExData("ex.lr_balance", CommonConsts.getExParam("ex.lr_balance"), lrBalanceInputList));
                        } finally {
                            mLockForEx.unlock();
                        }

                        lrBalanceInputList.clear();
                    }
                }
            }

            return;
        }

        /**
         *  周波数領域特徴量データをパースする
         * @param data 周波数領域特徴量データ
         */
        private void parseFreqDomain(String data) {

            String[] lineList=data.split(CommonConsts.BR);
            ArrayList<String> stressInputList = new ArrayList<String>();

            if(mServiceManageInfo.validExKey("ex.stress")) {
                for (int i = 0; i < lineList.length; i++) {

                    stressInputList.add(lineList[i]);
                }

                try {
                    mLockForEx.lock();
                    mListForEx.add(new ExData("ex.stress", CommonConsts.getExParam("ex.stress"), stressInputList));
                } finally {
                    mLockForEx.unlock();
                }
            }

            return;
        }

        private int addExReceiver(ExData exData) {

            String dataKey = exData.getDataKey();
            String dataParam = exData.getDataParam();
            String data = exData.getData();

            String[] keys = new String[1];
            keys[0] = dataKey;
            int res = mHitoeSdkAPI.addReceiver(null, keys, mDataReceiverCallback, dataParam, data);

            return res;
        }
    };

    private IAPIService.Stub mBinder = new IAPIService.Stub() {

        @Override
        public void registCallback(IAPIServiceCallback callback) throws RemoteException {
            Log.i(LOG_TAG, "State..registCallback:callback="+callback.toString());

            // 設定が変わっているかもしれないので再読み込み
            readConfig();

            mAPIServiceCallback = callback;

            // コールバックを取るようになったら
            releaseWakelock();

            return;
        }
        @Override
        public void unregistCallback() throws RemoteException {
            Log.i(LOG_TAG, "State..unregistCallback");

            mAPIServiceCallback = null;

            startWakelock();

            return;
        }

        @Override
        public void registExTypes(String types) throws RemoteException {
            Log.i(LOG_TAG, "State..registExTypes:types=" + types);

            try{

                mLockForEx.lock();

                ArrayList<String> keyList = new ArrayList<String>();
                String[] exTypes = types.split(CommonConsts.BR);
                for(int i=0; i < exTypes.length; i++) {
                    keyList.add(exTypes[i]);
                }
                mServiceManageInfo.setAvailableExKeyList(keyList);
            }finally {

                mLockForEx.unlock();
            }

            return;
        }

        @Override
        public void unregistExTypes() throws RemoteException {

            try{

                mLockForEx.lock();

                mServiceManageInfo.clearAvailableExKeyList();
                mListForEx.clear();
                mFlagForEx = false;
            }finally {

                mLockForEx.unlock();
            }

            return;
        }

        @Override
        public boolean getMeasureFlag() throws RemoteException {

            return mCurrentMeasure;
        }

        @Override
        public int getAvailableSensor(String sensorType, String param) throws RemoteException {
            Log.i(LOG_TAG, "State..getAvailableSensor:sensorType="+sensorType+",param="+param);

            int res = mHitoeSdkAPI.getAvailableSensor(sensorType, param);

            return res;
        }

        @Override
        public int connect(String sensorType, String sensorId, String connectionMode, String param) throws RemoteException {
            Log.i(LOG_TAG, "State..connect:sensorType="+sensorType+",sensorId="+sensorId+",connectionMode="+connectionMode+",param="+param);

            mDataLogger.start();

            // 一時保存データの初期化
            mListForPosture = new ArrayList<String>();
            mLockForPosture = new ReentrantLock();
            mListForWalk = new ArrayList<String>();
            mLockForWalk = new ReentrantLock();
            mListForLRBalance = new ArrayList<String>();
            mLockForLRBalance = new ReentrantLock();
            mListForEx = new ArrayList<ExData>();
            mLockForEx = new ReentrantLock();
            mFlagForEx = false;

            int res = mHitoeSdkAPI.connect(sensorType, sensorId, connectionMode, param);

            return res;
        }

        @Override
        public int disconnect() throws RemoteException {
            Log.i(LOG_TAG, "State..disconnect");

            mDataLogger.stop();

            String sessionId = mServiceManageInfo.getSessionId();
            int res = mHitoeSdkAPI.disconnect(sessionId);

            return res;
        }

        @Override
        public int getAvailableData() throws RemoteException {
            Log.i(LOG_TAG, "State..getAvailableData");

            String sessionId = mServiceManageInfo.getSessionId();
            int res = mHitoeSdkAPI.getAvailableData(sessionId);

            return res;
        }
        @Override
        public int getAvailableEx() throws RemoteException {
            Log.i(LOG_TAG, "State..getAvailableEx");

            int res = mHitoeSdkAPI.getAvailableData(null);

            return res;
        }

        @Override
        public int addRawReceiver(String dataKey, String param) throws RemoteException {
            Log.i(LOG_TAG, "State..addRawReceiver:dataKey="+dataKey+",param="+param);

            String sessionId = mServiceManageInfo.getSessionId();
            String[] keys = dataKey.split(CommonConsts.BR);

            int res = mHitoeSdkAPI.addReceiver(sessionId, keys, mDataReceiverCallback, param, null);

            return res;
        }

        @Override
        public int addBaReceiver(String dataKey, String param) throws RemoteException {
            Log.i(LOG_TAG, "State..addReceiver:dataKey="+dataKey+",param="+param);

            String sessionId = mServiceManageInfo.getSessionId();
            String[] keys = dataKey.split(CommonConsts.BR);

            int res = mHitoeSdkAPI.addReceiver(sessionId, keys, mDataReceiverCallback, param, null);

            return res;
        }

        @Override
        public int addExReceiver(String dataKey, String dataParam, String data) throws RemoteException {
            Log.i(LOG_TAG, "State..addExReceiver:dataKey=" + dataKey + ",param=" + dataParam + ",data=" + data);

            int res = CommonConsts.RES_ID_SUCCESS;

            // 拡張の場合は待ちに積む
            ArrayList<String> dataList  = new ArrayList<String>();
            String[] datas = data.split(CommonConsts.BR);
            for(int i=0; i < datas.length; i++) {
                dataList.add(datas[i]);
            }

            try {
                mLockForEx.lock();
                mListForEx.add(new ExData(dataKey, dataParam, dataList));
            } finally {
                mLockForEx.unlock();
            }

            return res;
        }

        @Override
        public int removeRawReceiver() throws RemoteException {
            Log.i(LOG_TAG, "State..removeRawReceiver");

            String connectionId = mServiceManageInfo.getRawConnectionId();
            int res = mHitoeSdkAPI.removeReceiver(connectionId);

            return res;
        }

        @Override
        public int removeBaReceiver() throws RemoteException {
            Log.i(LOG_TAG, "State..removeBaReceiver");

            String connectionId = mServiceManageInfo.getBaConnectionId();
            int res = mHitoeSdkAPI.removeReceiver(connectionId);

            return res;
        }

        @Override
        public int getStatus() throws RemoteException {
            Log.i(LOG_TAG, "State..getStatus");

            int res = mHitoeSdkAPI.getStatus();

            return res;
        }

        @Override
        public void finish() throws RemoteException {
            Log.i(LOG_TAG, "State..finish");

            // コールバックを解除
            mAPIServiceCallback = null;

            // disconnectを実行
            disconnect();

            return;
        }

        private void startWakelock() {
            Log.d(LOG_TAG, "State..startWakelock");
            synchronized(mLockObject) {
                if (mWakeLock == null) {
                    PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
                    mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, APIService.class.getName());
                    mWakeLock.acquire();
                }
            }
        }

        private void releaseWakelock() {
            Log.d(LOG_TAG, "State..releaseWakelock");
            synchronized(mLockObject) {
                if (mWakeLock != null) {
                    mWakeLock.release();
                    mWakeLock = null;
                }
            }

        }

    };

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(LOG_TAG, "State..onCreate");

        mContext = this;

        // 最初だけ実行
        mHitoeSdkAPI = HitoeSdkAPIImpl.getInstance(getApplicationContext());
        mHitoeSdkAPI.setAPICallback(mAPICallback);

        mServiceManageInfo = new ServiceManageInfo();

        mDataLogger = new DataLogger(CommonConsts.PREFERENCE_DEFAULT_FILEOUT);

        readConfig();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "State..onStartCommand");

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(LOG_TAG, "State..onDestroy");

        //mHitoeSdkAPI = null;
        //mServiceManageInfo = null;
    }

    // Binder
    @Override
    public IBinder onBind(Intent intent) {
Log.d(LOG_TAG, "State..onBind");
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
Log.d(LOG_TAG, "State..onUnbind");

        return true;
    }
    @Override
    public void onRebind(Intent intent) {
Log.d(LOG_TAG, "State..onRebind");

        return;
    }

    public void readConfig() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean fileout = sharedPreferences.getBoolean(CommonConsts.PREFERENCE_KEY_FILEOUT, CommonConsts.PREFERENCE_DEFAULT_FILEOUT);
        mDataLogger.setOutFlag(fileout);
        if(fileout) {
            mDataLogger.start();
        } else {
            mDataLogger.stop();
        }
    }
}
