/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.data;

import jp.co.docomo.sdk.hitoe.sample.CommonConsts;

/**
 * 心電データクラス
 */
public class ECGData {
    public long m_timestamp = -1;
    public double[] m_ecg_list = new double[8];

    public ECGData() {}
    public ECGData(String val) {

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

        String[] list = val.split(CommonConsts.COMMA, -1);
        this.m_timestamp = Long.parseLong(list[0]);
        String[] ecgList = list[1].split(CommonConsts.COLON, -1);
        for(int i = 0; i < ecgList.length; i++) {

            m_ecg_list[i] = Double.valueOf(ecgList[i]);
        }
    }
}
