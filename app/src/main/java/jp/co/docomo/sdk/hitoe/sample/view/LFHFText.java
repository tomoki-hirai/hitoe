/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import jp.co.docomo.sdk.hitoe.sample.CommonConsts;
import jp.co.docomo.sdk.hitoe.sample.R;
import jp.co.docomo.sdk.hitoe.sample.data.LFHFData;

/**
 * ストレス推定表示クラス
 */
public class LFHFText {

    // LogCatのTAG
    final String TAG = this.getClass().getSimpleName();

    // メインActivity
    private Activity mActivity;

    // ストレス推定結果
    private String mLFHFVal;

    // ストレス推定表示
    private TextView mLFHFTextView;
    // ストレス推定表示背景
    private GradientDrawable mLFHFGradientDrawable;

    // データ管理のためのロック
    private ReentrantLock mLock;

    // 更新スケジュール
    private ScheduledExecutorService mExecutor;

    /**
     * コンストラクタ
     * @param activity メインアクティビティ
     */
    public LFHFText(Activity activity) {
        mActivity = activity;

        mLFHFVal = null;

        mLFHFTextView = (TextView)mActivity.findViewById(R.id.LFHF_TEXT_VIEW);
        mLFHFGradientDrawable = (GradientDrawable)mLFHFTextView.getBackground();

        mExecutor = null;
        mLock = new ReentrantLock();
    }

    /**
     * ストレス推定結果登録
     * @param val 推定結果
     */
    public void addLFHF(String val) {

        try {
            mLock.lock();
            mLFHFVal = val;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 表示更新
     * @return 更新の有無
     */
    public boolean updateView() {

        try {
            final LFHFData lfhfData;
            final int score_RGB;
            final int score_R;
            final int score_G;
            final int score_B;

            try {
                mLock.lock();
                lfhfData = new LFHFData(mLFHFVal);
                mLFHFVal = null;
            } finally {
                mLock.unlock();
            }

            if (lfhfData.m_timestamp == -1) {

                return false;
            }
            score_RGB = (int) (150 * (lfhfData.m_lfhf / 5));
            if(105 + score_RGB < 255) {
                score_R = 105 + score_RGB;
            } else {
                score_R = 255;
            }
            if(255 - score_RGB > 0) {
                score_G = 255 - score_RGB;
                score_B = 255 - score_RGB;
            } else {
                score_G = 0;
                score_B = 0;
            }

            mLFHFTextView.post(new Runnable() {
                public void run() {
                    mLFHFTextView.setText("LF/HF:" + String.valueOf(lfhfData.m_lfhf));
                    mLFHFGradientDrawable.setColors(new int[]{0xFFCDFFFF, Color.rgb(score_R, score_G, score_B)});
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

            }, CommonConsts.LFHF_TEXT_UPDATE_CYCLE_TIME, CommonConsts.LFHF_TEXT_UPDATE_CYCLE_TIME, TimeUnit.MILLISECONDS);
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
