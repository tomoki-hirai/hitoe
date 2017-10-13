/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.data;

import java.util.ArrayList;

import jp.co.docomo.sdk.hitoe.sample.CommonConsts;

/**
 * 拡張データクラス
 */
public class ExData {

    private String dataKey;
    private String dataParam;
    private String data;

    public ExData(String dataKey, String dataParam, ArrayList<String> dataList) {

        this.dataKey = dataKey;
        this.dataParam = dataParam;
        StringBuilder dataStringBuilder = new StringBuilder();
        for(int i=0; i < dataList.size(); i++) {
            if(dataStringBuilder.length() > 0) {
                dataStringBuilder.append(CommonConsts.BR);
            }
            dataStringBuilder.append(dataList.get(i));
        }
        this.data=dataStringBuilder.toString();
    }
    public ExData(String dataKey, String dataParam, String data) {

        this.dataKey = dataKey;
        this.dataParam = dataParam;
        this.data=data;
    }

    public String getDataKey() {
        return dataKey;
    }
    public String getDataParam() {
        return dataParam;
    }
    public String getData() {
        return data;
    }
}
