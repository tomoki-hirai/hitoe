/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.data;

/**
 * 心拍データクラス
 */
public class HRData {
    public long m_timestamp = -1;
    public double m_hr = -1;

    public HRData(){}
    public HRData(String val) {

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
        this.m_hr = Double.parseDouble(list[1]);
    }
}
