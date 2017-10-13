/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.data;

/**
 * RRIデータクラス
 */
public class RRIData {
    public long m_timestamp = -1;
    public int m_rri = -1;

    public RRIData(){}
    public RRIData(String val) {

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
        this.m_rri = Integer.parseInt(list[1]);
    }
}
