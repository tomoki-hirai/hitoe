package jp.co.docomo.sdk.hitoe.sample;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Kooriyama on 2016/02/24.
 */
public class ConfigActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ConfigFragment()).commit();
    }
}
