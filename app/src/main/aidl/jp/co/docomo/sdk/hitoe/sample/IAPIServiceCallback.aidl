// IAPIServiceCallback.aidl
package jp.co.docomo.sdk.hitoe.sample;

// Declare any non-default types here with import statements

interface IAPIServiceCallback {
    void onResponse(int api_id, int response_id, String responseString);
    void onDataReceive(String connectionId, int response_id, String dataKey, String rawData);
}
