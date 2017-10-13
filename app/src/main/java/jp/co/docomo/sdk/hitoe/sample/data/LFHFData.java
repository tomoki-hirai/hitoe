/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.data;

/**
 * ストレス推定データクラス
 */
public class LFHFData {
    public long m_timestamp = -1;
    public double m_lfhf = -1;

    public LFHFData(){}
    public LFHFData(String val) {

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
        this.m_lfhf = Double.parseDouble(list[1]);
    }
}
