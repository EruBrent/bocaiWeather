package com.bocaiweather.app.Activity;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.bocaiweather.app.R;

/**
 * Created by oyj
 */
public class SettingFragment extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey){


        addPreferencesFromResource(R.xml.setting_layout);
    }

}

