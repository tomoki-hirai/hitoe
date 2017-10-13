/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.data;

/**
 * 姿勢推定データクラス
 */
public class PostureData {
    public long m_timestamp = -1;
    public String mType = null;
    public int m_back_forward = 0;
    public int m_left_right = 0;

    public PostureData(){}
    public PostureData(String val) {

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
        this.mType = list[1];
        this.m_back_forward = Integer.parseInt(list[2]);
        this.m_left_right = Integer.parseInt(list[3]);
    }
}
