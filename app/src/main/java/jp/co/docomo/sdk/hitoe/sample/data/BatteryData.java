/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.data;

/**
 * バッテリーデータクラス
 */
public class BatteryData {
    public long m_timestamp = -1;
    public double m_battery = -1;

    public BatteryData(){}
    public BatteryData(String val) {

        setData(val);
    }

    /**
     * データ登録
     * @param val 登録データ
     */
    public void setData(String val) {

        if(val == null) {

            return;
        }

        String[] list = val.split(",", -1);
        this.m_timestamp = Long.parseLong(list[0]);
        this.m_battery = Double.parseDouble(list[1]);
    }
}
