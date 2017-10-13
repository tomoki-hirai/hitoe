/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.view;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import jp.co.docomo.sdk.hitoe.sample.CommonConsts;
import jp.co.docomo.sdk.hitoe.sample.R;
import jp.co.docomo.sdk.hitoe.sample.data.BalanceData;
import jp.co.docomo.sdk.hitoe.sample.data.PostureData;
import jp.co.docomo.sdk.hitoe.sample.data.WalkStateData;

/**
 * 姿勢推定表示クラス
 */
public class PostureState {

    // LogCatのTAG
    final String TAG = this.getClass().getSimpleName();

    // メインActivity
    private Activity mActivity;

    // 姿勢推定結果
    private String mPostureVal;
    // 歩行状態推定結果
    private String mWalkStateVal;
    // 左右バランス推定結果
    private String mBalanceVal;

    // 姿勢推定のレイアウト
    private LinearLayout mPostureLinearLayout;

    // 姿勢推定表示
    private ImageView mPostureImageView;

    // 歩数値表示
    private TextView mWalkStepsTextView;
    // 歩行状態テキスト
    private TextView mWalkStateTextView;
    // 移動速度テキスト
    private TextView mWalkSpeedTextView;
    // 移動距離テキスト
    private TextView mWalkDistanceTextView;
    // 左右バランステキスト
    private TextView mWalkBalanceTextView;

    // データ管理のためのロック
    private ReentrantLock mLock;

    // 更新スケジュール
    private ScheduledExecutorService mExecutor;

    /**
     * コンストラクタ
     * @param activity メインアクティビティ
     */
    public PostureState(Activity activity) {
        mActivity = activity;

        mPostureVal = null;
        mWalkStateVal = null;
        mBalanceVal = null;

        mPostureLinearLayout = (LinearLayout)mActivity.findViewById(R.id.POSTURE_LINEAR_LAYOUT);
        mPostureImageView = (ImageView)mActivity.findViewById(R.id.POSTURE_IMAGE_VIEW);

        mWalkStateTextView = (TextView)mActivity.findViewById(R.id.WALK_STATE_TEXT_VIEW);
        mWalkStepsTextView = (TextView)mActivity.findViewById(R.id.WALK_STEPS_TEXT_VIEW);
        mWalkSpeedTextView = (TextView)mActivity.findViewById(R.id.WALK_SPEED_TEXT_VIEW);
        mWalkDistanceTextView =(TextView)mActivity.findViewById(R.id.WALK_DISTANCE_TEXT_VIEW);
        mWalkBalanceTextView = (TextView)mActivity.findViewById(R.id.WALK_LRBALANCE_TEXT_VIEW);

        mExecutor = null;
        mLock = new ReentrantLock();
    }

    /**
     * 姿勢推定結果登録
     * @param val 推定結果
     */
    public void addPosture(String val) {

        try {
            mLock.lock();
            mPostureVal = val;
        }finally {
            mLock.unlock();
        }
    }

    /**
     * 歩行状態推定結果登録
     * @param val 推定結果
     */
    public void addWalkState(String val) {

        try {
            mLock.lock();
            mWalkStateVal = val;
        }finally {
            mLock.unlock();
        }
    }

    /**
     * 左右バランス推定結果登録
     * @param val 推定結果
     */
    public void addLRBalance(String val) {

        try {
            mLock.lock();
            mBalanceVal = val;
        }finally {
            mLock.unlock();
        }
    }

    /**
     * 表示更新
     * @return 更新の有無
     */
    public boolean updateView() {

        try {
            PostureData postureData = null;
            WalkStateData walkStateData = null;
            BalanceData balanceData = null;

            try {
                mLock.lock();
                postureData = new PostureData(mPostureVal);
                walkStateData = new WalkStateData(mWalkStateVal);
                balanceData = new BalanceData(mBalanceVal);

                if (postureData.m_timestamp != -1 && walkStateData.m_timestamp != -1 && balanceData.m_timestamp != -1) {
                    mPostureVal = null;
                    mWalkStateVal = null;
                    mBalanceVal = null;
                }
            } finally {
                mLock.unlock();
            }

            final int drawable_id;
            final String stateString;
            final String stepsString;
            final String speedString;
            final String distanceString;
            final String balanceString;

            if (postureData.m_timestamp == -1 || walkStateData.m_timestamp == -1 || balanceData.m_timestamp == -1) {

                // どちらかがエラーであれば更新しない
                return false;
            }

            if (walkStateData.mState.equals("Walking")) {

                drawable_id = R.drawable.ic_posture_walking;
                stateString = "Walking";
            } else if (walkStateData.mState.equals("Running")) {

                drawable_id = R.drawable.ic_posture_running;
                stateString = "Running";
            } else {

                if (postureData.mType.equals("LyingLeft")) {

                    drawable_id = R.drawable.ic_posture_faceleft;
                    stateString = "LyingLeft";
                } else if (postureData.mType.equals("LyingRight")) {

                    drawable_id = R.drawable.ic_posture_faceright;
                    stateString = "LyingRight";
                } else if (postureData.mType.equals("LyingFaceUp")) {

                    drawable_id = R.drawable.ic_posture_faceup;
                    stateString = "FaceUp";
                } else if (postureData.mType.equals("LyingFaceDown")) {

                    drawable_id = R.drawable.ic_posture_facedown;
                    stateString = "FaceDown";
                } else {

                    // 立位
                    if (postureData.m_back_forward > CommonConsts.BACK_FORWARD_THRESHOLD) {

                        drawable_id = R.drawable.ic_posture_forward;
                        stateString = "Forward";
                    } else if (postureData.m_back_forward < -1 * CommonConsts.BACK_FORWARD_THRESHOLD) {

                        drawable_id = R.drawable.ic_posture_backward;
                        stateString = "BackWard";
                    } else if (postureData.m_left_right > CommonConsts.LEFT_RIGHT_THRESHOLD) {

                        drawable_id = R.drawable.ic_posture_leftside;
                        stateString = "LeftSide";
                    } else if (postureData.m_left_right < -1 * CommonConsts.LEFT_RIGHT_THRESHOLD) {

                        drawable_id = R.drawable.ic_posture_rightside;
                        stateString = "RightSide";
                    } else {

                        drawable_id = R.drawable.ic_posture_standing;
                        stateString = "Standing";
                    }
                }
            }

            stepsString = String.valueOf(walkStateData.m_steps);
            speedString = String.valueOf(walkStateData.m_speed);
            distanceString = String.valueOf(walkStateData.m_distance);
            balanceString = String.valueOf(balanceData.m_balance);

            mPostureLinearLayout.post(new Runnable() {
                public void run() {
                    mPostureImageView.setImageResource(drawable_id);

                    mWalkStateTextView.setText(stateString);
                    mWalkStepsTextView.setText(stepsString);
                    mWalkSpeedTextView.setText(speedString);
                    mWalkDistanceTextView.setText(distanceString);
                    mWalkBalanceTextView.setText(balanceString);
                    return;
                }
            });

        } catch (Exception e) {

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

            }, CommonConsts.POSTURE_STATE_UPDATE_CYCLE_TIME, CommonConsts.POSTURE_STATE_UPDATE_CYCLE_TIME, TimeUnit.MILLISECONDS);
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
