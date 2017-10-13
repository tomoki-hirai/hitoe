/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jp.co.docomo.sdk.hitoe.sample.data.ACCData;
import jp.co.docomo.sdk.hitoe.sample.data.AvailableSensorData;
import jp.co.docomo.sdk.hitoe.sample.data.BatteryData;
import jp.co.docomo.sdk.hitoe.sample.data.ECGData;
import jp.co.docomo.sdk.hitoe.sample.view.ACCChart;
import jp.co.docomo.sdk.hitoe.sample.view.ECGChart;
import jp.co.docomo.sdk.hitoe.sample.view.HRText;
import jp.co.docomo.sdk.hitoe.sample.view.LFHFText;
import jp.co.docomo.sdk.hitoe.sample.view.PostureState;

/**
 * メインクラス
 */
public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName();

    /**
     * デバッグログ出力
     * @param tag タグ
     * @param log ログ
     */
    public void debugLog(String tag, String log) {

        if(CommonConsts.DEBUG) {
            Log.d(tag, log);
        }
    }

    /**
     *  バックグラウンドからメッセージをToastする
     * @param message メッセージ文字列
     */
    private void showToastFromBackground(final String message) {
        mHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
    /**
     *  フォアグラウンドからメッセージをToastする
     * @param message メッセージ文字列
     */
    private void showToastFromForeground(String message) {

        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
    }

    // LogCatのTAG
    private String TAG = this.getClass().getSimpleName();
    // this
    private Activity mActivity;
    // バックグラウンドからToastする際のハンドラ
    private Handler mHandler;

    // Pinコード情報
    private String mPincode;
    // 管理情報
    private ManageInfo mManageInfo;
    // ファイルロガー
//    private DataLogger mDataLogger;

    // SDK
    //private HitoeSdkAPI mHitoeSdkAPI;

    // LFHFレイアウト
    private LFHFText mLFHFText;
    // 心拍レイアウト
    private HRText mHRText;
    //心拍画像
    private ImageView mHRImage;
    // 姿勢レイアウト
    private PostureState mPostureState;
    // 心電グラフレイアウト
    private ECGChart mECGChart;
    // 加速度グラフレイアウト
    private ACCChart mACCChart;

    // バッテリデータ
    private String mStrForBattery;


    // ダイアログ
    private LayoutInflater mLayoutInflater;
    // 利用可能センサー一覧ダイアログのレイアウト
    private View mAvailableSensorDialogLayout;
    // 利用可能センサー一覧のコメント
    private TextView mAvailableSensorDialogTextView;
    // 利用可能センサー一覧のラジオボタングループ
    private RadioGroup mAvailableSensorDialogRadioGroup;
    // PINCODE確認ダイアログのレイアウト
    private View mConnectDialogLayout;
    // PINCODE入力のテキスト入力
    private EditText mConnectDialogPincodeEditText;
    // 情報表示ダイアログのレイアウト
    private View mInfoDialogLayout;
    // 情報表示のテキスト
    private TextView mInfoDialogTextView;

    // 計測Start/Stopスイッチ
    private Switch mMeasureSwitch;

    private IAPIService mAPIService = null;

    SharedPreferences mSharedPreferences = null;

    /**
     *  起動関数
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Log.d(LOG_TAG, "State..onCreate");

        mActivity = this;
        mHandler = new Handler();
        mLayoutInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);

        mPincode = null;
        mManageInfo = new ManageInfo();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 設定初期化
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = mSharedPreferences.edit();
        boolean fileout = mSharedPreferences.getBoolean(CommonConsts.PREFERENCE_KEY_FILEOUT, CommonConsts.PREFERENCE_DEFAULT_FILEOUT);
        preferenceEditor.putBoolean(CommonConsts.PREFERENCE_KEY_FILEOUT, fileout);

        String retryTime = mSharedPreferences.getString(CommonConsts.PREFERENCE_KEY_DISCONNECT_RETRY_TIME, CommonConsts.PREFERENCE_DEFAULT_DISCONNECT_RETRY_TIME);
        preferenceEditor.putString(CommonConsts.PREFERENCE_KEY_DISCONNECT_RETRY_TIME, retryTime);

        String retryCount = mSharedPreferences.getString(CommonConsts.PREFERENCE_KEY_DISCONNECT_RETRY_COUNT, CommonConsts.PREFERENCE_DEFAULT_DISCONNECT_RETRY_COUNT);
        preferenceEditor.putString(CommonConsts.PREFERENCE_KEY_DISCONNECT_RETRY_COUNT, retryCount);

        preferenceEditor.commit();

        mLFHFText = new LFHFText(this);
        mHRText = new HRText(this);
        mPostureState = new PostureState(this);
        LinearLayout ecgLinearLayout = (LinearLayout) findViewById(R.id.ECG_LINEAR_LAYOUT);
        mECGChart = new ECGChart(this);
        ((LinearLayout) findViewById(R.id.ECG_LINEAR_LAYOUT)).addView(mECGChart.getGraphicalView());
        mACCChart = new ACCChart(this);
        ((LinearLayout) findViewById(R.id.ACC_LINEAR_LAYOUT)).addView(mACCChart.getGraphicalView());

        mMeasureSwitch = (Switch) findViewById(R.id.MEASURE_SWITCH);

        mHRImage = (ImageView) findViewById(R.id.HR_IMAGE_VIEW);

        //SampleApplication.isServiceStarted = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
Log.d(LOG_TAG, "State..onDestroy");

        if(SampleApplication.isFinished) {

            stopAPIService();

            SampleApplication.isServiceStarted = false;
            SampleApplication.isFinished = false;
        }
    }

    /**
     *  Resume関数
     */
    @Override
    public void onResume() {

        super.onResume();
Log.d(LOG_TAG, "State..onResume:" + mAPIService + "," + mCallback);

        if(!SampleApplication.isServiceStarted) {
            // 起動されていなければ

            // サービス起動
            startAPIService();
        }

        //　サービスバインド
        bindAPIService();
    }

    /**
     *  Pause関数
     */
    @Override
    public void onPause() {

        super.onPause();
Log.d(LOG_TAG, "State..onPause");

        mMeasureSwitch.setOnCheckedChangeListener(null);

        boolean measure_flag = false;

        try {
            measure_flag= mAPIService.getMeasureFlag();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(measure_flag) {
            // 計測開始していたら

            // スイッチを停止
            //mMeasureSwitch.setChecked(false);

            // 描画停止
            stopUpdate();
        }

        if(!SampleApplication.isFinished) {
            // コールバック解除
            try {
                mAPIService.unregistCallback();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        // サービスバインド解除
        unbindAPIService();
    }

    @Override
    protected  void onSaveInstanceState(Bundle outState) {
Log.d(LOG_TAG, "State..onSaveInstanceState");
        outState.putBoolean(CommonConsts.OUTSTATE_SERVICE_STARTED, SampleApplication.isServiceStarted);
        outState.putBoolean(CommonConsts.OUTSTATE_FINISHED, SampleApplication.isFinished);
    }
    @Override
    protected  void onRestoreInstanceState(Bundle savedInstanceState) {
Log.d(LOG_TAG, "State..onRestoreInstanceState");
        SampleApplication.isServiceStarted = savedInstanceState.getBoolean(CommonConsts.OUTSTATE_SERVICE_STARTED);
        SampleApplication.isFinished = savedInstanceState.getBoolean(CommonConsts.OUTSTATE_FINISHED);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {

            // ダイアログで終了確認
            showExitProcess();

            return true;
        }

        return false;
    }

    private void startAPIService() {
        Intent intent = new Intent(this, APIService.class);
        startService(intent);

        SampleApplication.isServiceStarted = true;
    }
    private void stopAPIService() {
        Intent intent = new Intent(this, APIService.class);
        stopService(intent);

        SampleApplication.isServiceStarted = false;
    }

    private void bindAPIService() {
        Intent intent = new Intent(this, APIService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    private void unbindAPIService() {

        unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
Log.d(LOG_TAG, "State..onServiceConnected");
            mAPIService = IAPIService.Stub.asInterface(service);

            boolean measure_flag = false;
            // コールバック登録
            try {
                measure_flag = mAPIService.getMeasureFlag();
                mAPIService.registCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if(measure_flag) {
                // 計測開始していたら

                // 計測中にボタンを変更
                mMeasureSwitch.setChecked(true);

                // 描画開始
                startUpdate();
            }

            mMeasureSwitch.setOnCheckedChangeListener(mCheckedChangeListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
Log.d(LOG_TAG, "State..onServiceDisconnected");
            mAPIService = null;
        }
    };

    private IAPIServiceCallback.Stub mCallback = new IAPIServiceCallback.Stub() {

        @Override
        public void onResponse(int api_id, int response_id, String responseString) throws RemoteException {

            //
            try {

                final StringBuilder messageTextBuilder = new StringBuilder();

                debugLog(TAG, "CbCallback:apiId=" + String.valueOf(api_id) + ",responseId=" + String.valueOf(response_id) + ",resonseObject=" + responseString.replace(CommonConsts.BR, CommonConsts.VB));

                switch (api_id) {

                    case CommonConsts.API_ID_GET_AVAILABLE_SENSOR:

                        if (response_id != CommonConsts.RES_ID_SUCCESS || responseString == null) {

                            mAvailableSensorDialogTextView.post(new Runnable() {
                                public void run() {
                                    mAvailableSensorDialogTextView.setText("利用可能なセンサーが見つかりません");
                                }
                            });

                            break;
                        } else {

                            mAvailableSensorDialogTextView.post(new Runnable() {
                                public void run() {
                                    mAvailableSensorDialogTextView.setText("利用可能センサーを取得しました");
                                }
                            });
                        }

                        if (responseString.length() == 0) {

                            break;
                        }

                        final String[] sensorList = responseString.split(CommonConsts.BR, -1);
                        mAvailableSensorDialogRadioGroup.post(new Runnable() {
                            public void run() {

                                // リストからRadioButtonを作って追加
                                boolean first_flag = true;
                                for (int i = 0; i < sensorList.length; i++) {

                                    String sensorStr = sensorList[i].trim();
                                    if(sensorStr.length() == 0) {

                                        continue;
                                    }
                                    if(sensorStr.indexOf("memory_setting") > 0) {
                                        if(!CommonConsts.DEBUG) {
                                            continue;
                                        }
                                        // memory_settingの場合は二つに分ける
                                        RadioButton radio = new RadioButton(mActivity.getBaseContext());
                                        radio.setText(sensorStr+"hr");
                                        radio.setTextColor(Color.BLACK);

                                        mAvailableSensorDialogRadioGroup.addView(radio);
                                        if (first_flag) {
                                            radio.setChecked(true);
                                            first_flag = false;
                                        }

                                        radio = new RadioButton(mActivity.getBaseContext());
                                        radio.setText(sensorStr+"rri");
                                        radio.setTextColor(Color.BLACK);

                                        mAvailableSensorDialogRadioGroup.addView(radio);
                                    } else if(sensorStr.indexOf("memory_get") > 0) {
                                        if(!CommonConsts.DEBUG) {
                                            continue;
                                        }
                                        // memory_getの場合は二つに分ける
                                        RadioButton radio = new RadioButton(mActivity.getBaseContext());
                                        radio.setText(sensorStr.replace("raw.saved_hr|raw.saved_rri", "raw.saved_hr"));
                                        radio.setTextColor(Color.BLACK);

                                        mAvailableSensorDialogRadioGroup.addView(radio);
                                        if (first_flag) {
                                            radio.setChecked(true);
                                            first_flag = false;
                                        }

                                        radio = new RadioButton(mActivity.getBaseContext());
                                        radio.setText(sensorStr.replace("raw.saved_hr|raw.saved_rri", "raw.saved_rri"));
                                        radio.setTextColor(Color.BLACK);

                                        mAvailableSensorDialogRadioGroup.addView(radio);
                                    } else {

                                        RadioButton radio = new RadioButton(mActivity.getBaseContext());
                                        radio.setText(sensorStr);
                                        radio.setTextColor(Color.BLACK);

                                        mAvailableSensorDialogRadioGroup.addView(radio);
                                        if (first_flag) {
                                            radio.setChecked(true);
                                            first_flag = false;
                                        }
                                    }
                                }
                            }
                        });

                        break;
                    case CommonConsts.API_ID_CONNECT:

                        if (response_id == CommonConsts.RES_ID_SENSOR_DISCONECT_NOTICE) {
                            // 接続が切れた場合
                            messageTextBuilder.append("センサー接続が途切れました。(" + response_id + ")\n" + responseString);
                            showToastFromBackground(messageTextBuilder.toString());

                            // 失敗のため停止
                            setMeasureSwitch(false);
                            setMeasureSwitchEnabled(true);

                            break;
                        } else if(response_id == CommonConsts.RES_ID_SENSOR_CONNECT_NOTICE) {
                            // 再接続された場合
                            messageTextBuilder.append("センサー接続が再開しました。(" + response_id + ")\n" + responseString);
                            showToastFromBackground(messageTextBuilder.toString());

                            break;
                        } else if(response_id == CommonConsts.RES_ID_SENSOR_TEMPORARILY_DISCONNECT) {
                            // 一時切断
                            messageTextBuilder.append("センサー接続が一時切断しました。(" + response_id + ")\n" + responseString);
                            showToastFromBackground(messageTextBuilder.toString());

                            break;
                        } else if(response_id == CommonConsts.RES_ID_SENSOR_RECONNECT_FAILURE) {
                            // 再接続失敗
                            //messageTextBuilder.append("センサー接続が一時切断しました。(" + response_id + ")\n" + responseString);
                            //showToastFromBackground(messageTextBuilder.toString());

                            break;
                        } else if(response_id == CommonConsts.RES_ID_SENSOR_RECONNECT) {
                            // 再接続成功
                            messageTextBuilder.append("センサー接続が再接続しました。(" + response_id + ")\n" + responseString);
                            showToastFromBackground(messageTextBuilder.toString());

                            break;

                        } else if (response_id != CommonConsts.RES_ID_SENSOR_CONNECT) {

                            // その他接続できなかった場合
                            messageTextBuilder.append("センサー接続に失敗しました。(" + response_id + ")\n" + responseString);
                            showToastFromBackground(messageTextBuilder.toString());

                            // 失敗のため停止
                            setMeasureSwitch(false);
                            setMeasureSwitchEnabled(true);

                            break;
                        }

                        // 接続成功なのでpincodeを保存
                        SharedPreferences sharedPreferences = getSharedPreferences(CommonConsts.PINCODE_PREFERENCE, MODE_PRIVATE);
                        if (sharedPreferences != null) {

                            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
                            preferenceEditor.putString(mManageInfo.getPincodePreferenceKey(), mPincode);
                            preferenceEditor.commit();
                        }

                        if(mManageInfo.mConnectMode.equals("memory_setting")) {

                            messageTextBuilder.append("メモリアクセス設定をしました。(" + response_id + ")\n" + responseString);
                            showToastFromBackground(messageTextBuilder.toString());

                            // 自動的に切断
                            setMeasureSwitch(false);
                            setMeasureSwitchEnabled(true);

                            // メモリ設定の場合はここで終わる。
                            break;
                        }

                        messageTextBuilder.append("センサー接続しました。(" + response_id + ")\n" + responseString);
                        showToastFromBackground(messageTextBuilder.toString());

                        // Switch活性化
                        setMeasureSwitchEnabled(true);

                        // センサー接続できたらレシーバ登録
                        addRawReceiverProcess();

                        break;
                    case CommonConsts.API_ID_DISCONNECT:

                        mManageInfo.init();

                        messageTextBuilder.append("センサー切断しました。(" + response_id + ")\n" + responseString);
                        showToastFromBackground(messageTextBuilder.toString());

                        break;
                    case CommonConsts.API_ID_GET_AVAILABLE_DATA:

                        if (response_id != CommonConsts.RES_ID_SUCCESS || responseString == null) {

                            messageTextBuilder.append("確認に失敗しました。(" + response_id + ")\n" + responseString);
                        } else {

                            messageTextBuilder.append(responseString);
                        }
                        showToastFromBackground(messageTextBuilder.toString());

                        break;
                    case CommonConsts.API_ID_ADD_RECIVER:

                        if (response_id != CommonConsts.RES_ID_SUCCESS || responseString == null) {

                            // レシーバ登録失敗
                            setMeasureSwitch(false);
                            setMeasureSwitchEnabled(true);

                            break;
                        }

                        if(responseString.startsWith(CommonConsts.RAW_CONNECTION_PREFFIX) &&
                                mManageInfo.mMemoryGetData == null) {

                            // Rawデータの応答でsavedモードでなければ次の基本分析を登録する
                            addBaReceiverProcess();
                        } else if(responseString.startsWith(CommonConsts.BA_CONNECTION_PREFFIX)) {

                            // Baデータの応答であれば拡張の設定を登録する
                            mAPIService.registExTypes(CommonConsts.AVAILABLE_EX_DATA_STR);
                        } else {

                            // 拡張の結果であれば特に
                        }

                        break;
                    case CommonConsts.API_ID_REMOVE_RECEIVER:

                        if (response_id != CommonConsts.RES_ID_SUCCESS || responseString == null) {

                            // レシーバ削除失敗
                            setMeasureSwitch(false);
                            setMeasureSwitchEnabled(true);

                            break;
                        }

                        // 終了プロセス
                        if(responseString.startsWith(CommonConsts.BA_CONNECTION_PREFFIX)) {
                            removeRawReceiverProcess();
                        } else if(responseString.startsWith(CommonConsts.RAW_CONNECTION_PREFFIX)) {
                            // コネクションがなくなったらdisconnect
                            disconnectProcess();
                        }

                        break;
                    case CommonConsts.API_ID_GET_STATUS:

                        if (response_id != CommonConsts.RES_ID_SUCCESS || responseString == null) {

                            messageTextBuilder.append("確認に失敗しました。(" + response_id + ")\n" + responseString);
                        } else {

                            messageTextBuilder.append(responseString);
                        }
                        showToastFromBackground(messageTextBuilder.toString());
                        break;
                    default:

                        break;
                }
            }catch(Exception e) {

                //mDataLogger.setThrowable(e);
            }

            return;
        }

        @Override
        public void onDataReceive(String connectionId, int response_id, String dataKey, String rawData) throws RemoteException {

            // データ応答
            debugLog(TAG, "DataCallback:connectId=" + connectionId + ",dataKey=" + dataKey + ",response_id="+response_id+",rawData=" + rawData.replace("\n", ","));
            MyLogger myLogger=MyLogger.getInstance();
            try {

                // ログ出力
//                mDataLogger.setDataStr(dataKey, rawData);


                // データ受信時のコールバック
                if (dataKey.equals("raw.ecg")) {
                    String[] split = rawData.split("\n",-1);
                    for (int i=0;i<split.length;i++){
                        Log.d("ECGログ",split[i]);
                        myLogger.writeFile(split[i],dataKey);
                        parseECGStr(split[i]);
                    }
//                    parseECGStr(rawData);
                } else if (dataKey.equals("raw.acc")) {
                    parseACCStr(rawData);

                } else if (dataKey.equals("raw.rri")) {
                    String[] split = rawData.split("\n",-1);
                    for (int i=0;i<split.length;i++){
                        Log.d("RRIログ",split[i]);
                        myLogger.writeFile(split[i],dataKey);
                    }
                } else if (dataKey.equals("raw.bat")) {
                    parseBatStr(rawData);

                } else if (dataKey.equals("raw.hr")) {
                    parseHRStr(rawData);
                } else if (dataKey.equals("raw.leadoff")) {
                    Log.d("leadoff", "leadoff " + rawData);
                    parseLeadOffStr(rawData);

                } else if (dataKey.equals("raw.saved_hr")) {

                    // 最後のフラグを見て終了であれば切断する
                    if(response_id != CommonConsts.RES_ID_CONTINUE) {

                        setMeasureSwitch(false);
                        setMeasureSwitchEnabled(true);
                    }
                } else if (dataKey.equals("raw.saved_rri")) {

                    // 最後のフラグを見て終了であれば切断する
                    if(response_id != CommonConsts.RES_ID_CONTINUE) {

                        setMeasureSwitch(false);
                        setMeasureSwitchEnabled(true);
                    }
                } else if (dataKey.equals("ba.extracted_rri")) {

                } else if (dataKey.equals("ba.cleaned_rri")) {
                    String[] split = rawData.split("\n",-1);
                    for (int i=0;i<split.length;i++){
                        Log.d("RRIログ",split[i]);
                        myLogger.writeFile(split[i],dataKey);
                    }
                } else if (dataKey.equals("ba.interpolated_rri")) {
                    String[] split = rawData.split("\n",-1);
                    for (int i=0;i<split.length;i++){
                        Log.d("i_RRIログ",split[i]);
                        myLogger.writeFile(split[i],dataKey);
                    }
                } else if (dataKey.equals("ba.freq_domain")) {

                } else if (dataKey.equals("ba.time_domain")) {

                } else if (dataKey.equals("ex.stress")) {
                    parseStress(rawData);

                } else if (dataKey.equals("ex.posture")) {
                    parsePosture(rawData);

                } else if (dataKey.equals("ex.walk")) {
                    parseWalk(rawData);

                } else if (dataKey.equals("ex.lr_balance")) {
                    parseLRBalance(rawData);

                }

            }catch(Exception e) {

                //mDataLogger.setThrowable(e);
            }

            return;
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

                mECGChart.setECG(ecgData.m_timestamp, ecgData.m_ecg_list);
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
                mACCChart.setACC(accData.m_timestamp, accData.m_acc_list);
            }

            return;
        }

        /**
         * Batデータをパースする
         * @param data Batデータ
         */
        private void parseBatStr(String data) {

            String[] lineList=data.split(CommonConsts.BR);
            mStrForBattery = lineList[lineList.length - 1];

            return;
        }
        /**
         * HRデータをパースする
         * @param data HRデータ
         */
        private void parseHRStr(String data) {

            String[] lineList=data.split(CommonConsts.BR);
            mHRText.addHR(lineList[lineList.length - 1]);

            return;
        }

        /**
         * leadoffデータをパースする
         * @param data leadoff
         */
        private void parseLeadOffStr(String data) {

            String[] lineList=data.split(CommonConsts.BR);
            for (String line : lineList) {
                String[] arr = line.split(",");
                if(arr.length == 2 && arr[1].equals("0")) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHRImage.setImageResource(R.drawable.ic_hr);
                        }
                    });
                }else if (arr.length == 2 && arr[1].equals("1")) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHRImage.setImageResource(R.drawable.ic_hr_disable);
                        }
                    });
                } else {
                    //do nothing
                }

            }
            return;
        }

        /**
         *  ストレス推定データをパースする
         * @param data ストレス推定データ
         */
        private void parseStress(String data) {

            if(data.length() == 0) {
                return;
            }
            String[] lineList=data.split(CommonConsts.BR);

            mLFHFText.addLFHF(lineList[lineList.length - 1]);

            return;
        }

        /**
         *  姿勢推定データをパースする
         * @param data 姿勢推定データ
         */
        private void parsePosture(String data) {

            if(data.length() == 0) {
                return;
            }
            String[] lineList=data.split(CommonConsts.BR);

            mPostureState.addPosture(lineList[lineList.length - 1]);

            return;
        }

        /**
         *  歩行状態推定データをパースする
         * @param data 歩行状態推定データ
         */
        private void parseWalk(String data) {

            if(data.length() == 0) {
                return;
            }
            String[] lineList=data.split(CommonConsts.BR);

            mPostureState.addWalkState(lineList[lineList.length - 1]);

            return;
        }

        /**
         *  歩行時左右バランス推定データをパースする
         * @param data 歩行時左右バランス推定データ
         */
        private void parseLRBalance(String data) {

            if(data.length() == 0) {
                return;
            }
            String[] lineList=data.split(CommonConsts.BR);

            mPostureState.addLRBalance(lineList[lineList.length - 1]);

            return;
        }
    };

    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean is_checked) {
            boolean measure_flag = false;
            try {
                measure_flag = mAPIService.getMeasureFlag();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            switch (buttonView.getId()) {
                case R.id.MEASURE_SWITCH:
                    // 計測ボタン
                    if (is_checked) {

                        // ボタン無効
                        setMeasureSwitchEnabled(false);

                        // ON
                        //showToastFromForeground("Measure_Switch to ON");

                        // 描画開始
                        startUpdate();

                        if(!measure_flag) {
                            // 利用可能センサー取得
                            getAvailableSensorProcess();
                        }
                    } else {

                        // OFF
                        //showToastFromForeground("Measure_Switch to OFF");

                        // 描画停止
                        stopUpdate();

                        if(measure_flag) {
                            // 計測中であれば停止
                            disconnectProcess();
                        }
                    }
                    break;
            }
        }
    };

    /**
     *  メニュー表示
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(CommonConsts.DEBUG) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }
    /**
     *  メニュー選択
     * @param menuItem 選択したメニュー
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int id = menuItem.getItemId();

        boolean measure_flag = false;
        try {
            measure_flag = mAPIService.getMeasureFlag();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        switch(id) {

            case R.id.action_available_data:
                if(!measure_flag) {
                    showToastFromBackground("未接続です。");
                } else {
                    getAvailableDataProcess();
                }
                break;
            case R.id.action_available_ex:
                if(!measure_flag) {
                    showToastFromBackground("未接続です。");
                } else {
                    getAvailableExProcess();
                }
                break;
            case R.id.action_battery:
                if(!measure_flag) {
                    showToastFromBackground("未接続です。");
                } else {
                    showBatteryProcess();
                }
                break;
            case R.id.action_status:
                if(!measure_flag) {
                    showToastFromBackground("未接続です。");
                } else {
                    getStatusProcess();
                }
                break;
//            case R.id.action_config:
//                Log.d(LOG_TAG, "Config..");
//                Intent intent = new Intent(this, ConfigActivity.class);
//                startActivity(intent);
//
//                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    /**
     *  描画開始
     */
    private void startUpdate() {

        // 疲労度推定テキスト
        mLFHFText.start();
        // 心拍テキスト
        mHRText.start();
        // 姿勢推定イメージ
        mPostureState.start();
        // 心電グラフ
        mECGChart.start();
        // 加速度グラフ
        mACCChart.start();

        return;
    }
    /**
     *  描画停止
     */
    private void stopUpdate() {

        // 疲労度推定テキスト
        mLFHFText.stop();
        // 心拍テキスト
        mHRText.stop();
        // 姿勢推定イメージ
        mPostureState.stop();
        // 心電グラフ
        mECGChart.stop();
        // 加速度グラフ
        mACCChart.stop();

        return;
    }
    /**
     *  利用可能センサー一覧取得
     */
    public void getAvailableSensorProcess() {

        // 利用可能センサー一覧表示ダイアログ
        int response_id = CommonConsts.RES_ID_FAILURE;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        // 管理情報を初期化
        mManageInfo.init();

        mAvailableSensorDialogLayout = mLayoutInflater.inflate(R.layout.available_sensor_dialog, (ViewGroup)findViewById(R.id.AVAILABLE_SENSOR_DIALOG_ROOT));
        mAvailableSensorDialogTextView = (TextView)mAvailableSensorDialogLayout.findViewById(R.id.AVAILABLE_SENSOR_TEXT_VIEW);
        mAvailableSensorDialogRadioGroup = (RadioGroup)mAvailableSensorDialogLayout.findViewById(R.id.AVAILABLE_SENSOR_RADIO_GROUP);

        dialogBuilder.setTitle("センサー選択");
        dialogBuilder.setView(mAvailableSensorDialogLayout);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // センサー接続
                int check_id = mAvailableSensorDialogRadioGroup.getCheckedRadioButtonId();
                if (check_id != -1) {
                    String checkedSensor = ((RadioButton) mAvailableSensorDialogLayout.findViewById(check_id)).getText().toString();

                    // 選択されていれば次の通信につなげる
                    Log.d(TAG, "Select Sensor:" + checkedSensor);

                    // hitoe_D01,hitoe01_1,realtime,ecg|acc|rri|bat|hr\n
                    AvailableSensorData AvailableSensorData = new AvailableSensorData(checkedSensor);
                    mManageInfo.setSensorInfo(AvailableSensorData);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // キャンセルなら何もしない
            }
        });

        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                if (mManageInfo.mDeviceId != null) {

                    StringBuilder paramBuilder = new StringBuilder();
                    if (mManageInfo.mConnectMode.equals("memory_setting")) {

                        //メモリ設定であればパラメータ追加
                        if (paramBuilder.length() > 0) {
                            paramBuilder.append(CommonConsts.BR);
                        }
                        paramBuilder.append("recording_data=" + mManageInfo.mMemorySettingData);
                    } else if (mManageInfo.mConnectMode.equals("memory_get")) {


                    } else {

//                        int retryTime = Integer.parseInt(mSharedPreferences.getString(CommonConsts.PREFERENCE_KEY_DISCONNECT_RETRY_TIME, CommonConsts.PREFERENCE_DEFAULT_DISCONNECT_RETRY_TIME));
                        String retryTime = mSharedPreferences.getString(CommonConsts.PREFERENCE_KEY_DISCONNECT_RETRY_TIME, CommonConsts.PREFERENCE_DEFAULT_DISCONNECT_RETRY_TIME);
                        if (retryTime != "") {

                            paramBuilder.append("disconnect_retry_time=" + retryTime);
                        }
//                        int retryCount = Integer.parseInt(mSharedPreferences.getString(CommonConsts.PREFERENCE_KEY_DISCONNECT_RETRY_COUNT, CommonConsts.PREFERENCE_DEFAULT_DISCONNECT_RETRY_COUNT));
                        String retryCount = mSharedPreferences.getString(CommonConsts.PREFERENCE_KEY_DISCONNECT_RETRY_COUNT, CommonConsts.PREFERENCE_DEFAULT_DISCONNECT_RETRY_COUNT);
                        if (retryCount != "") {

                            if (paramBuilder.length() > 0) {
                                paramBuilder.append(CommonConsts.BR);
                            }
                            paramBuilder.append("disconnect_retry_count=" + retryCount);
                        }
                        if (CommonConsts.CONNECT_NOPACKET_RETRY_TIME > 0) {

                            if (paramBuilder.length() > 0) {
                                paramBuilder.append(CommonConsts.BR);
                            }
                            paramBuilder.append("nopacket_retry_time=" + CommonConsts.CONNECT_NOPACKET_RETRY_TIME);
                        }
                    }

                    connectProcess(paramBuilder.toString());
                    return;
                }

                setMeasureSwitch(false);
                setMeasureSwitchEnabled(true);
            }
        });

        dialogBuilder.show();

        StringBuilder paramStringBuilder = new StringBuilder();
        // 利用可能センサーを問い合わせ
        if(CommonConsts.GET_AVAILABLE_SENSOR_PARAM_SEARCH_TIME != -1) {
            paramStringBuilder.append("search_time=" + String.valueOf(CommonConsts.GET_AVAILABLE_SENSOR_PARAM_SEARCH_TIME));
        }

        debugLog(TAG, "HitoeSDKAPI getAvailableSensor:deviceType=" + CommonConsts.GET_AVAILABLE_SENSOR_DEVICE_TYPE + ",param=" + paramStringBuilder.toString());
        //response_id = mHitoeSdkAPI.getAvailableSensor(CommonConsts.GET_AVAILABLE_SENSOR_DEVICE_TYPE, paramStringBuilder.toString());
        try {
            response_id = mAPIService.getAvailableSensor(CommonConsts.GET_AVAILABLE_SENSOR_DEVICE_TYPE, paramStringBuilder.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        debugLog(TAG, "HitoeSDKAPI getAvailableSensor:ResponseID=" + String.valueOf(response_id));

        if (response_id != CommonConsts.RES_ID_SUCCESS) {
            if (response_id == CommonConsts.RES_ID_PERMISSION_NOT_GRANTED) {
                showToastFromBackground("Androidの設定を確認してください。");
            }

            mAvailableSensorDialogTextView.post(new Runnable() {
                public void run() {

                    mAvailableSensorDialogTextView.setText("利用可能センサーの問い合わせに失敗しました。");
                }
            });
        }
    }

    /**
     *  センサー接続
     *  @param paramStr センサー接続のためのパラメータ
     */
    public void connectProcess(String paramStr) {

        final StringBuilder paramStringBuilder = new StringBuilder();
        paramStringBuilder.append(paramStr);

        // preferenceからpincodeを取得
        SharedPreferences sharedPreferences = getSharedPreferences(CommonConsts.PINCODE_PREFERENCE, MODE_PRIVATE);
        if (sharedPreferences != null) {

            mPincode = sharedPreferences.getString(mManageInfo.getPincodePreferenceKey(), "");
        }

        // pincodeが取れればそれを使ってconnectする
        if(mPincode.length() > 0) {

            if(paramStringBuilder.length() > 0) {

                paramStringBuilder.append(CommonConsts.BR);
            }
            paramStringBuilder.append("pincode=");
            paramStringBuilder.append(mPincode);

            String param = paramStringBuilder.toString();
            debugLog(TAG, "HitoeSDKAPI connect:deviceType=" + mManageInfo.mDeviceType + ",deviceId=" + mManageInfo.mDeviceId + ",connectMode=" + mManageInfo.mConnectMode + ",param=" + param.replace("\n", "|"));
            //int response_id = mHitoeSdkAPI.connect(mManageInfo.mDeviceType, mManageInfo.mDeviceId, mManageInfo.mConnectMode, param);
            int response_id = CommonConsts.RES_ID_FAILURE;
            try {
                response_id = mAPIService.connect(mManageInfo.mDeviceType, mManageInfo.mDeviceId, mManageInfo.mConnectMode, param);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            debugLog(TAG, "HitoeSDKAPI connect:ResponseID=" + String.valueOf(response_id));

            if(response_id != CommonConsts.RES_ID_SUCCESS) {

                // 接続失敗
                if (response_id == CommonConsts.RES_ID_PERMISSION_NOT_GRANTED) {
                    showToastFromBackground("Androidの設定を確認してください。");
                } else {
                    showToastFromBackground("接続に失敗しました。");
                }

                // 失敗のため停止
                setMeasureSwitch(false);
                setMeasureSwitchEnabled(true);
            }
            return;
        }

        // pincodeが取れなければダイアログを表示
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("pincode入力");
        mConnectDialogLayout = mLayoutInflater.inflate(R.layout.connect_dialog, (ViewGroup)findViewById(R.id.CONNECT_DIALOG_ROOT));
        dialogBuilder.setView(mConnectDialogLayout);
        mConnectDialogPincodeEditText = (EditText)mConnectDialogLayout.findViewById(R.id.CONNECT_DIALOG_PINCODE_EDIT_TEXT);
        mConnectDialogPincodeEditText.setInputType(InputType.TYPE_CLASS_TEXT);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mPincode = mConnectDialogPincodeEditText.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPincode = "";
            }
        });
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                // 入力された値で次のconnect
                if (mPincode.length() > 0) {

                    if(paramStringBuilder.length() > 0) {

                        paramStringBuilder.append(CommonConsts.BR);
                    }
                    paramStringBuilder.append("pincode=");
                    paramStringBuilder.append(mPincode);

                    String param = paramStringBuilder.toString();
                    debugLog(TAG, "HitoeSDKAPI connect:deviceType=" + mManageInfo.mDeviceType + ",deviceId=" + mManageInfo.mDeviceId + ",connectMode=" + mManageInfo.mConnectMode + ",param=" + param.replace("\n", "|"));
                    //response_id = mHitoeSdkAPI.connect(mManageInfo.mDeviceType, mManageInfo.mDeviceId, mManageInfo.mConnectMode, param);
                    int response_id = CommonConsts.RES_ID_FAILURE;
                    try {
                        response_id = mAPIService.connect(mManageInfo.mDeviceType, mManageInfo.mDeviceId, mManageInfo.mConnectMode, param);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    debugLog(TAG, "HitoeSDKAPI connect:ResponseID=" + String.valueOf(response_id));

                    if(response_id != CommonConsts.RES_ID_SUCCESS) {

                        // 接続失敗
                        if (response_id == CommonConsts.RES_ID_PERMISSION_NOT_GRANTED) {
                            showToastFromBackground("Androidの設定を確認してください。");
                        } else {
                            showToastFromBackground("接続に失敗しました。");
                        }

                        // 失敗のため停止
                        setMeasureSwitch(false);
                        setMeasureSwitchEnabled(true);
                    }
                } else {

                    // キャンセル
                    setMeasureSwitch(false);
                    setMeasureSwitchEnabled(true);
                }
            }
        });

        dialogBuilder.show();

        return;
    }

    /**
     *  Rawデータレシーバ登録
     */
    public void addRawReceiverProcess() {

        // データ種別文字列作成
        String keyString = null;
        String paramString = null;
        if(mManageInfo.mMemoryGetData != null) {
            keyString = mManageInfo.mMemoryGetData;
            paramString = "";
        } else {
            keyString = CommonConsts.AVAILABLE_RAW_DATA_STR;
            String[] keys = keyString.split(CommonConsts.BR);
            ArrayList<String> keyList = new ArrayList<String>();
            for (int i = 0; i < keys.length; i++) {
                keyList.add(keys[i]);
            }

            paramString = CommonConsts.getRawParam(keyList);
        }
        debugLog(TAG, "HitoeSDKAPI addRawReceiver:dataKey=" + keyString.replace(CommonConsts.BR, CommonConsts.VB) + ",mCallback=" + mCallback.toString() + ",param=" + paramString.replace("\n", "|"));

        //response_id = mHitoeSdkAPI.addReceiver(mManageInfo.mSessionId, keys, mDataReceiverCallback, paramString, null);
        int response_id = CommonConsts.RES_ID_FAILURE;
        try {
            response_id = mAPIService.addRawReceiver(keyString, paramString);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        debugLog(TAG, "HitoeSDKAPI addRawReceiver:ResponseID=" + String.valueOf(response_id));

        if (response_id != CommonConsts.RES_ID_SUCCESS) {
            showToastFromBackground("Rawデータレシーバ設定に失敗しました。(" + String.valueOf(response_id) + ")");

            setMeasureSwitch(false);
            setMeasureSwitchEnabled(true);
            return;
        }

        return;
    }

    /**
     *  基本分析レシーバ登録
     */
    public void addBaReceiverProcess() {

        // データ種別文字列作成
        String keyString = CommonConsts.AVAILABLE_BA_DATA_STR;
        String[] keys = keyString.split(CommonConsts.BR);
        ArrayList<String> keyList = new ArrayList<String>();
        for(int i = 0; i < keys.length; i++) {
            keyList.add(keys[i]);
        }

        String paramString = CommonConsts.getBaParam(keyList);

        debugLog(TAG, "HitoeSDKAPI addBaReceiver:dataKey=" + keyString.replace(CommonConsts.BR, CommonConsts.VB) + ",mCallback=" + mCallback.toString() + ",param=" + paramString.replace("\n", "|"));
        //response_id = mHitoeSdkAPI.addReceiver(mManageInfo.mSessionId, keys, mDataReceiverCallback, paramString, null);
        int response_id = CommonConsts.RES_ID_FAILURE;
        try {
            response_id = mAPIService.addBaReceiver(keyString, paramString);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        debugLog(TAG, "HitoeSDKAPI addBaReceiver:ResponseID=" + String.valueOf(response_id) + ",param=" + paramString.replace("\n", "|"));
        if (response_id != CommonConsts.RES_ID_SUCCESS) {
            showToastFromBackground("基本分析レシーバ設定に失敗しました。(" + String.valueOf(response_id) + ")");

            setMeasureSwitch(false);
            setMeasureSwitchEnabled(true);
            return;
        }

        return;
    }

    /**
     *  Rawデータレシーバ削除
     */
    public void removeRawReceiverProcess() {

        int response_id = CommonConsts.RES_ID_FAILURE;
        try {
            response_id = mAPIService.removeRawReceiver();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return;
    }

    /**
     *  基本分析レシーバ削除
     */
    public void removeBaReceiverProcess() {

        int response_id = CommonConsts.RES_ID_FAILURE;
        try {
            response_id = mAPIService.removeBaReceiver();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return;
    }


    /**
     *  利用可能データ取得
     */
    public void getAvailableDataProcess() {

        debugLog(TAG, "HitoeSDKAPI getAvailableData");
        //response_id = mHitoeSdkAPI.getAvailableData(mManageInfo.mSessionId);
        int response_id = CommonConsts.RES_ID_FAILURE;
        try {
            response_id = mAPIService.getAvailableData();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        debugLog(TAG, "HitoeSDKAPI getAvailableData:ResponseID=" + String.valueOf(response_id));
        if(response_id != CommonConsts.RES_ID_SUCCESS) {

            showToastFromBackground("利用可能データ一覧の取得に失敗しました。(" + String.valueOf(response_id) + ")");
        }

        return;
    }

    /**
     *  利用可能拡張分析取得
     */
    public void getAvailableExProcess() {

        debugLog(TAG, "HitoeSDKAPI getAvailableEx");
        //response_id = mHitoeSdkAPI.getAvailableData(mManageInfo.mSessionId);
        int response_id = CommonConsts.RES_ID_FAILURE;
        try {
            response_id = mAPIService.getAvailableEx();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        debugLog(TAG, "HitoeSDKAPI getAvailableEx:ResponseID=" + String.valueOf(response_id));
        if(response_id != CommonConsts.RES_ID_SUCCESS) {

            showToastFromBackground("利用可能拡張分析一覧の取得に失敗しました。(" + String.valueOf(response_id) + ")");
        }

        return;
    }

    /**
     *  状態取得
     */
    public void getStatusProcess() {

        debugLog(TAG, "HitoeSDKAPI getStatus");
        //response_id = mHitoeSdkAPI.getStatus();
        int response_id = CommonConsts.RES_ID_FAILURE;
        try {
            response_id = mAPIService.getStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        debugLog(TAG, "HitoeSDKAPI getStatus:ResponseID=" + String.valueOf(response_id));
        if(response_id != CommonConsts.RES_ID_SUCCESS) {

            showToastFromForeground("利用状況の取得に失敗しました。("+String.valueOf(response_id)+")");
        }

        return;
    }

    /**
     *  センサー切断
     */
    public void disconnectProcess() {

        debugLog(TAG, "HitoeSDKAPI disconnect");
        //response_id = mHitoeSdkAPI.disconnect(mManageInfo.mSessionId);
        int response_id = CommonConsts.RES_ID_FAILURE;
        try {
            response_id = mAPIService.disconnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        debugLog(TAG, "HitoeSDKAPI disconnect:ResponseID=" + String.valueOf(response_id));
        if(response_id != CommonConsts.RES_ID_SUCCESS) {

            showToastFromBackground("センサー切断に失敗しました。(" + String.valueOf(response_id)+")");
            return;
        }

        mManageInfo.init();

        return;
    }

    /**
     *  バッテリ状態表示
     */
    public void showBatteryProcess() {

        // すでに取ってあるものを表示するだけ
        BatteryData batteryData = new BatteryData(mStrForBattery);

        if(batteryData.m_timestamp == -1) {
            showToastFromBackground("―");
        } else {
            showToastFromBackground(String.valueOf(batteryData.m_battery));

        }
    }

    /**
     *  終了確認
     */
    public void showExitProcess() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        String versionName = "";

        mInfoDialogLayout = mLayoutInflater.inflate(R.layout.info_dialog, (ViewGroup)findViewById(R.id.INFO_DIALOG_ROOT));
        mInfoDialogTextView = (TextView)mInfoDialogLayout.findViewById(R.id.INFO_DIALOG_TEXT_VIEW);

        View dialogLayout = mLayoutInflater.inflate(R.layout.info_dialog, (ViewGroup)findViewById(R.id.INFO_DIALOG_ROOT));

        dialogBuilder.setTitle("終了確認");
        dialogBuilder.setView(dialogLayout);

        TextView infoText = (TextView) dialogLayout.findViewById(R.id.INFO_DIALOG_TEXT_VIEW);
        infoText.setText(R.string.action_exit);

        dialogBuilder.setPositiveButton("終了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int which) {
                mMeasureSwitch.setOnCheckedChangeListener(null);

                // サービスを停止して終了
                SampleApplication.isFinished = true;
                boolean measure_flag = false;
                try {
                    measure_flag = mAPIService.getMeasureFlag();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                if(measure_flag) {
                    try {
                        mAPIService.finish();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int which) {
                // ダイアログを閉じる
            }
        });

        dialogBuilder.show();
    }

    /**
     * スイッチを管理状態に応じて切り替える
     */
    private void setMeasureSwitch(final boolean flag) {

        mMeasureSwitch.post(new Runnable() {
            @Override
            public void run() {

                mMeasureSwitch.setChecked(flag);
            }
        });

        return;
    }

    /**
     * スイッチの活性状態を切り替える
     */
    private void setMeasureSwitchEnabled(final boolean flag) {

        mMeasureSwitch.post(new Runnable() {
            @Override
            public void run() {

                mMeasureSwitch.setEnabled(flag);
            }
        });

        return;
    }

}
