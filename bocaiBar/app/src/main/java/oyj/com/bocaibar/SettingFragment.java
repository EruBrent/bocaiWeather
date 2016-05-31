package oyj.com.bocaibar;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by oyj on 2016/4/27.
 */
public class SettingFragment extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey){

        addPreferencesFromResource(R.xml.setting_layout);
    }

}

