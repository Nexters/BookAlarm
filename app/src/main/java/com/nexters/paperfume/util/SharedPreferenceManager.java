package com.nexters.paperfume.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.nexters.paperfume.App;

/**
 * Created by sangyeonK on 2016-08-14.
 */
public class SharedPreferenceManager {
    private static SharedPreferenceManager ourInstance = new SharedPreferenceManager();

    public static SharedPreferenceManager getInstance() {
        return ourInstance;
    }

    private SharedPreferenceManager() {
        mSharedPreferences = App.getInstance().getSharedPreferences(PAPERFUME_PREFERENCES, Context.MODE_PRIVATE);
    }

    private static final String PAPERFUME_PREFERENCES = "PAPERFUME_PREFERENCES" ;

    private SharedPreferences mSharedPreferences;

    public void setBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, -1);
    }

    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, -1L);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
