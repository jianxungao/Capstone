package com.biz.timux.capstone.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.biz.timux.capstone.R;

/**
 * Created by gaojianxun on 16/7/8.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);
    }
}
