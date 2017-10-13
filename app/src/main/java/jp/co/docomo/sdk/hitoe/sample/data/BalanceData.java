/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.data;

/**
 * 左右バランス推定データクラス
 */
public class BalanceData {
    public long m_timestamp = -1;
    public float m_balance = 0;

    public BalanceData(){}
    public BalanceData(String val) {

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
        this.m_balance = Float.parseFloat(list[1]);
    }
}
