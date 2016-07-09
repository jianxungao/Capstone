package com.biz.timux.capstone.ui;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by gaojianxun on 16/7/8.
 */
public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
 }
