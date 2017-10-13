/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.view;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import jp.co.docomo.sdk.hitoe.sample.CommonConsts;
import jp.co.docomo.sdk.hitoe.sample.R;
import jp.co.docomo.sdk.hitoe.sample.data.HRData;

/**
 * 心拍表示クラス
 */
public class HRText {

    // LogCatのTAG
    final String TAG = this.getClass().getSimpleName();

    // メインActivity
    private Activity mActivity;

    // 心拍
    private String mHRVal;

    // 心拍表示
    private TextView mHRTextView;

    // データ管理のためのロック
    private ReentrantLock mLock;

    // 更新スケジュール
    private ScheduledExecutorService mExecutor;

    /**
     * コンストラクタ
     * @param activity メインアクティビティ
     */
    public HRText(Activity activity) {
        mActivity = activity;

        mHRVal = null;

        mHRTextView = (TextView)mActivity.findViewById(R.id.HR_TEXT_VIEW);

        mExecutor = null;
        mLock = new ReentrantLock();
    }

    /**
     * 心拍登録
     * @param val 心拍
     */
    public void addHR(String val) {

        try {
            mLock.lock();
            mHRVal = val;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 表示更新
     * @return 更新の有無
     */
    public boolean updateView() {

        //Log.d(TAG, "HR updateView: Start");
        try {
            final HRData hrData;
            try {
                mLock.lock();
                hrData = new HRData(mHRVal);
                mHRVal = null;
            } finally {
                mLock.unlock();
            }

            if (hrData.m_timestamp == -1) {

                return false;
            }

            mHRTextView.post(new Runnable() {
                public void run() {
                    mHRTextView.setText(String.valueOf(hrData.m_hr));
                    return;
                }
            });
        }catch(Exception e) {
            Log.d(TAG, e.toString());
        }

        return true;

    }

    /**
     * スケジュール開始
     */
    public void start() {
        if(mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
            mExecutor.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    updateView();
                }

            }, CommonConsts.HR_TEXT_UPDATE_CYCLE_TIME, CommonConsts.HR_TEXT_UPDATE_CYCLE_TIME, TimeUnit.MILLISECONDS);
        }

    }

    /**
     * スケジュール停止
     */
    public void stop(){
        if(mExecutor == null) {
            return;
        }
        mExecutor.shutdown();
        try {
            mExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mExecutor = null;
    }

}
