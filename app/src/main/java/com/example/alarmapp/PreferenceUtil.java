package com.example.alarmapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    private static final String CONFIG_FILE_NAME = "alarm_setting";

    private SharedPreferences preference;

    public PreferenceUtil(Context context) {
        preference = context.getSharedPreferences(PreferenceUtil.CONFIG_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLond(String key) {
        return preference.getLong(key, 0);
    }
    public void delete(String key) {
        SharedPreferences.Editor editor = preference.edit();
        editor.remove(key);
        editor.apply();
    }
}
