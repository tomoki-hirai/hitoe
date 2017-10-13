package jp.co.docomo.sdk.hitoe.sample;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

/**
 * Created by Yamamoto on 2016/09/16.
 */
public class ConfigFragment  extends PreferenceFragment {
    // テキストボックスPreferenceの PreferenceChangeリスナー
    private Preference.OnPreferenceChangeListener editTextPreference_OnPreferenceChangeListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    return editTextPreference_OnPreferenceChange(preference, newValue);
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.config);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        // 設定情報の初期表示
        EditTextPreference editTextDisconnectRetryTime = (EditTextPreference) getPreferenceScreen().findPreference(CommonConsts.PREFERENCE_KEY_DISCONNECT_RETRY_TIME);
        editTextDisconnectRetryTime.setSummary(editTextDisconnectRetryTime.getText());
        EditTextPreference editTextDisconnectRetryCount = (EditTextPreference) getPreferenceScreen().findPreference(CommonConsts.PREFERENCE_KEY_DISCONNECT_RETRY_COUNT);
        editTextDisconnectRetryCount.setSummary(editTextDisconnectRetryCount.getText());

        // リスナー設定
        editTextDisconnectRetryTime.setOnPreferenceChangeListener(editTextPreference_OnPreferenceChangeListener);
        editTextDisconnectRetryCount.setOnPreferenceChangeListener(editTextPreference_OnPreferenceChangeListener);
    }

    private boolean editTextPreference_OnPreferenceChange(Preference preference, Object newValue) {
        boolean ret = true;
        String input = newValue.toString();
        preference.setSummary(input);
        return ret;
    }
}
