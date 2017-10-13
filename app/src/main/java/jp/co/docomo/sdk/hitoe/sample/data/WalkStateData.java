/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.data;

/**
 * 歩行状態推定データクラス
 */
public class WalkStateData {
    public long m_timestamp = -1;
    public int m_steps = 0;
    public int m_cadence = 0;
    public double m_rule = 0;
    public String mState = null;
    public double m_earth_time = 0;
    public double m_speed = 0;
    public double m_distance = 0;

    public WalkStateData(){}
    public WalkStateData(String val) {

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
        this.m_steps = Integer.parseInt(list[1]);
        this.m_cadence = Integer.parseInt(list[2]);
        this.m_rule = Double.parseDouble(list[3]);
        this.mState = list[4];
        this.m_earth_time = Double.parseDouble(list[5]);
        this.m_speed = Double.parseDouble(list[6]);
        this.m_distance = Double.parseDouble(list[7]);
    }
}
