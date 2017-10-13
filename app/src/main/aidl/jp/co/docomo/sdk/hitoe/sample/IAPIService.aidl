// IAPIService.aidl
package jp.co.docomo.sdk.hitoe.sample;

import jp.co.docomo.sdk.hitoe.sample.IAPIServiceCallback;

// Declare any non-default types here with import statements

interface IAPIService {
    void registCallback(IAPIServiceCallback callback);
    void unregistCallback();
    void registExTypes(String types);
    void unregistExTypes();
    boolean getMeasureFlag();
    int getAvailableSensor(String sensorType, String param);
    int connect(String sensorType, String sensorId, String connectionMode, String param);
    int disconnect();
    int getAvailableData();
    int getAvailableEx();
    int addRawReceiver(String dataKey, String param);
    int addBaReceiver(String dataKey, String param);
    int addExReceiver(String dataKey, String param, String data);
    int removeRawReceiver();
    int removeBaReceiver();
    int getStatus();
    void finish();
}
