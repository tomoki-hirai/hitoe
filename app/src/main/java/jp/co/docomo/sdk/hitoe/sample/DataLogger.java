/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ファイル出力クラス
 */
public class DataLogger {

    // LogCatのTAG
    final String TAG = this.getClass().getSimpleName();

    // 起動時刻
    private Calendar mCalendar;
    // ログデータリスト
    private ArrayList<DataSet> mDataList;
    // 応答データリスト
    private ArrayList<ResponseSet> mResponseList;
    // ログデータ管理のためのロック
    private ReentrantLock mLock;
    // 出力ディレクトリ
    private String mOutputDir;
    // ファイル出力スケジュール
    private ScheduledExecutorService mExecutor;
    // ファイル出力間隔
    private int mExetime = 60000;
    // ファイル出力
    private boolean mOutFlag = false;

    /**
     * ログデータクラス
     */
    class DataSet {

        // データキー
        public String mKey;
        // データ
        public String mData;
        // 登録時刻
        public String mRegistDate;

        public DataSet(String key, String data) {

            Date d = new Date();
            this.mKey = key;
            this.mData = data;
            this.mRegistDate = String.valueOf(d.getTime());
        }
    }

    /**
     * 応答データクラス
     */
    class ResponseSet {

        // APIコード
        public int mAPIId;
        // 応答コード
        public int mResponseId;
        // 応答文字列
        public String mResponseStr;
        // 登録時刻
        public String mRegistDate;

        public ResponseSet(int apiId, int responseId, String responseStr) {

            Date d = new Date();
            this.mAPIId = apiId;
            this.mResponseId = responseId;
            this.mResponseStr = responseStr;
            this.mRegistDate = String.valueOf(d.getTime());
        }
    }

    /**
     * コンストラクタ
     */
    public DataLogger(boolean outFlag) {

        File outputFile;
        mLock = new ReentrantLock();
        mOutputDir = Environment.getExternalStorageDirectory() + "/" + CommonConsts.DEBUG_DIR;
        mOutFlag = outFlag;

        if(mOutFlag) {
            outputFile = new File(mOutputDir);
            if (!outputFile.exists()) {
                outputFile.mkdir();
            }
        }
    }

    public void setOutFlag(boolean outFlag) {
        mOutFlag = outFlag;

        if(mOutFlag) {
            File outputFile = new File(mOutputDir);
            if (!outputFile.exists()) {
                outputFile.mkdir();
            }
        }
    }

    /**
     * ログデータ登録
     * @param key データキー
     * @param data データ
     */
    public void setDataStr(String key, String data) {

        if(!mOutFlag) {

            // デバッグでなければ何もしない
            return;
        }

        try{
            mLock.lock();
            mDataList.add(new DataSet(key, data));
        }finally {

            mLock.unlock();
        }

        return;
    }

    /**
     * 応答データ登録
     * @param apiId APIコード
     * @param responseId レスポンスコード
     * @param responseStr レスポンス文字列
     */
    public void setResponseStr(int apiId, int responseId, String responseStr) {

        if(!mOutFlag) {

            // デバッグでなければ何もしない
            return;
        }

        try{
            mLock.lock();
            mResponseList.add(new ResponseSet(apiId, responseId, responseStr));
        }finally {

            mLock.unlock();
        }

        return;
    }

    /**
     * ファイル出力
     */
    private void logData() {
        HashMap<String, ArrayList<String>> currentDataHash;
        String formatDateString;
        String typeString;
        ArrayList<String> valList;
        String fileName;
        File logFile;
        FileWriter fileWriter;

        try {
            //Log.d(TAG, "Logger logData:Start");
            ArrayList<DataSet> currentDataList;
            try {
                mLock.lock();

                currentDataList = mDataList;
                mDataList = new ArrayList<DataSet>();
            } finally {
                mLock.unlock();
            }

            currentDataHash = new HashMap<String, ArrayList<String>>();
            for (int i = 0; i < currentDataList.size(); i++) {

                String key = currentDataList.get(i).mKey;
                String val = currentDataList.get(i).mData;
                String[] vals = val.split(CommonConsts.BR);
                String regist = currentDataList.get(i).mRegistDate;

                if (!currentDataHash.containsKey(key)) {

                    currentDataHash.put(key, new ArrayList<String>());
                }

                for(int j = 0; j < vals.length; j++) {

                    currentDataHash.get(key).add(regist+","+vals[j]);
                }
            }

            formatDateString =String.format("%d%02d%02d%02d%02d%02d",
                    mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH)+1,
                    mCalendar.get(Calendar.DATE),
                    mCalendar.get(Calendar.HOUR_OF_DAY),
                    mCalendar.get(Calendar.MINUTE),
                    mCalendar.get(Calendar.SECOND));

            for (Map.Entry<String, ArrayList<String>> e : currentDataHash.entrySet()) {

                typeString = e.getKey();
                valList = e.getValue();
                fileName = mOutputDir + File.separator + typeString + "_" + formatDateString+CommonConsts.LOG_FILE_SUFFIX;

                try {
                    logFile = new File(fileName);
                    fileWriter = new FileWriter(logFile, true);

                    for (int i = 0; i < valList.size(); i++) {
                        fileWriter.write(valList.get(i) + "\n");
                    }
                    fileWriter.close();

                } catch (FileNotFoundException exception) {
                    Log.e(TAG, "logData:"+exception.toString());
                } catch (UnsupportedEncodingException exception) {
                    Log.e(TAG, "logData:"+exception.toString());
                }
            }

        }catch(Exception exception){

            Log.e(TAG, "logData:"+exception.toString());
        }
    }

    /**
     * 応答データファイル出力
     */
    private void logResponseData() {
        HashMap<String, ArrayList<String>> currentDataHash;
        String formatDateString;
        String typeString;
        ArrayList<String> valList;
        String fileName;
        File logFile;
        FileWriter fileWriter;

        try {
            //Log.d(TAG, "Logger logData:Start");
            ArrayList<ResponseSet> currentDataList;
            try {
                mLock.lock();

                currentDataList = mResponseList;
                mResponseList = new ArrayList<ResponseSet>();
            } finally {
                mLock.unlock();
            }

            String key = "response";
            currentDataHash = new HashMap<String, ArrayList<String>>();
            for (int i = 0; i < currentDataList.size(); i++) {

                ResponseSet currentResponseSet = currentDataList.get(i);

                if (!currentDataHash.containsKey(key)) {

                    currentDataHash.put(key, new ArrayList<String>());
                }

                currentDataHash.get(key).add(currentResponseSet.mRegistDate+","+String.valueOf(currentResponseSet.mAPIId)+","+String.valueOf(currentResponseSet.mResponseId)+","+currentResponseSet.mResponseStr);
            }

            formatDateString =String.format("%d%02d%02d%02d%02d%02d",
                    mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH)+1,
                    mCalendar.get(Calendar.DATE),
                    mCalendar.get(Calendar.HOUR_OF_DAY),
                    mCalendar.get(Calendar.MINUTE),
                    mCalendar.get(Calendar.SECOND));

            for (Map.Entry<String, ArrayList<String>> e : currentDataHash.entrySet()) {

                typeString = e.getKey();
                valList = e.getValue();
                fileName = mOutputDir + File.separator + typeString + "_" + formatDateString+CommonConsts.LOG_FILE_SUFFIX;

                try {
                    logFile = new File(fileName);
                    fileWriter = new FileWriter(logFile, true);

                    for (int i = 0; i < valList.size(); i++) {
                        fileWriter.write(valList.get(i) + "\n");
                    }
                    fileWriter.close();

                } catch (FileNotFoundException exception) {
                    Log.e(TAG, "logResponse:"+exception.toString());
                } catch (UnsupportedEncodingException exception) {
                    Log.e(TAG, "logResponse:"+exception.toString());
                }
            }

        }catch(Exception exception){

            Log.e(TAG, "logResponse:"+exception.toString());
        }
    }

    /**
     * スケジュール開始
     */
    public void start() {

        if(mExecutor == null) {

            mCalendar = Calendar.getInstance();
            mDataList = new ArrayList<DataSet>();
            mResponseList = new ArrayList<ResponseSet>();

            mExecutor = Executors.newSingleThreadScheduledExecutor();
            mExecutor.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    Calendar currentCalendar = Calendar.getInstance();
                    if(mCalendar.get(Calendar.DATE) != currentCalendar.get(Calendar.DATE)) {
                        // 1日過ぎたらファイルを変える
                        mCalendar = currentCalendar;
                    }

                    logData();
                    logResponseData();
                }

            }, mExetime, mExetime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * スケジュール停止
     */
    public void stop(){

        if(mExecutor == null) {
            return;
        }
        // 止める前に出力
        logData();
        logResponseData();

        mExecutor.shutdown();
        try {
            mExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mExecutor = null;
    }
}
