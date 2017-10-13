/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.data;

import jp.co.docomo.sdk.hitoe.sample.CommonConsts;

/**
 * 加速度データクラス
 */
public class ACCData {
    public long m_timestamp = -1;
    public double[] m_acc_list = new double[3];

    public ACCData(){}
    public ACCData(String val) {

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
        String[] accList = list[1].split(CommonConsts.COLON, -1);
        for(int i = 0; i < accList.length; i++) {

            m_acc_list[i] = Double.valueOf(accList[i]);
        }
    }
}
