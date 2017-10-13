/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample;

import android.util.Log;

import java.util.ArrayList;

import jp.co.docomo.sdk.hitoe.sample.data.AvailableSensorData;

/**
 * 管理データクラス
 */
public class ManageInfo {

    // LogCatのTAG
    final String TAG = this.getClass().getSimpleName();

    // デバイス識別子
    public String mDeviceType = null;
    // 個体識別子
    public String mDeviceId = null;
    // 接続モード
    public String mConnectMode = null;
    // メモリ設定
    public String mMemorySettingData = null;
    public String mMemoryGetData = null;

    public ManageInfo() {
        init();
    }
    /**
     *  初期化
     */
    public void init() {

        // センサー名
        mDeviceType = null;
        // センサーID
        mDeviceId = null;
        // センサーモード
        mConnectMode = null;
        // メモリ設定
        mMemorySettingData = null;
        mMemoryGetData = null;
    }

    /**
     *  センサー情報の登録
     */
    public void setSensorInfo(AvailableSensorData availableSensorData) {

        setSensorInfo(availableSensorData.mDeviceType, availableSensorData.mDeviceId, availableSensorData.mConnectMode, availableSensorData.mAvailableData);
    }
    /**
     *  センサー情報の登録(個別指定)
     */
    public void setSensorInfo(String deviceType, String deviceId, String connectMode, String availableData) {

        this.mDeviceType = deviceType;
        this.mDeviceId = deviceId;
        this.mConnectMode = connectMode;
        if (connectMode.equals("memory_setting")) {

            this.mMemorySettingData = availableData;
        } else if(connectMode.equals("memory_get")) {
            this.mMemoryGetData = availableData;
        }
    }
    /**
     *  PreferenceのPINコード保存キー作成
     *  @return PINコード保存キー文字列
     */
    public String getPincodePreferenceKey() {

        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(mDeviceType);
        keyBuilder.append("_");
        keyBuilder.append(mDeviceId);

        return keyBuilder.toString();
    }
}
