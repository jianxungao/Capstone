package com.biz.timux.capstone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.biz.timux.capstone.R;
import com.biz.timux.capstone.sync.CountrySyncAdapter;

/**
 * Created by gaojianxun on 16/7/8.
 */
public class Utility {

    public static String formatTemperature(Context context, double temperature) {

        String suffix = "\u00B0";

        return String.format(context.getString(R.string.format_temperature), temperature);
    }

    public static String getFavoriteCountry(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(context.getString(R.string.pref_country_key),
                               context.getString(R.string.pref_country_default));

    }
}
