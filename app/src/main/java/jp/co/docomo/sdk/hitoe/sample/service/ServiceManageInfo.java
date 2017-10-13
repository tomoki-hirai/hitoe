package jp.co.docomo.sdk.hitoe.sample.service;

import android.app.Service;

import java.util.ArrayList;

/**
 * Created by Kooriyama on 2016/02/19.
 */
public class ServiceManageInfo {

    // LogCatのTAG
    final String TAG = this.getClass().getSimpleName();

    // 利用拡張分析データ
    public ArrayList<String> mAvailableExKeyList = new ArrayList<String>();

    // セッションID
    public String mSessionId = null;
    // RAWデータコネクションID
    public String mRawConnectionId = null;
    // 基本分析コネクションID
    public String mBaConnectionId = null;

    public ServiceManageInfo() {
        init();
    }

    /**
     * 初期化
     */
    public void init() {

        // 利用可能Exデータ
        mAvailableExKeyList = new ArrayList<String>();

        // セッションID
        mSessionId = null;
        // RAWデータコネクションID
        mRawConnectionId = null;
        //
        mBaConnectionId = null;
    }

    public void clearAvailableExKeyList() {

        this.mAvailableExKeyList.clear();
    }
    public void setAvailableExKeyList(ArrayList<String> availableExKeyList) {

        this.mAvailableExKeyList = availableExKeyList;
    }
    public ArrayList<String> getAvailableExKeyList() {

        return this.mAvailableExKeyList;
    }
    public void setSessionId(String sessionId) {
        this.mSessionId = sessionId;
    }
    public String getSessionId() {
        return this.mSessionId;
    }
    public void setRawConnectionId(String rawConnectionId) {
        this.mRawConnectionId = rawConnectionId;
    }
    public String getRawConnectionId() {
        return this.mRawConnectionId;
    }
    public void setBaConnectionId(String baConnectionId) {
        this.mBaConnectionId = baConnectionId;
    }
    public String getBaConnectionId() {
        return this.mBaConnectionId;
    }

    public boolean validExKey(String dataKey) {
        if(this.mAvailableExKeyList.contains(dataKey)) {
            return true;
        }
        return false;
    }
}
