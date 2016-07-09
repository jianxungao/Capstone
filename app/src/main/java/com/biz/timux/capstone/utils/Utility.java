package com.biz.timux.capstone.utils;

import android.content.Context;

import com.biz.timux.capstone.R;

/**
 * Created by gaojianxun on 16/7/8.
 */
public class Utility {

    public static String formatTemperature(Context context, double temperature) {

        String suffix = "\u00B0";

        return String.format(context.getString(R.string.format_temperature), temperature);
    }
}
