package com.nexters.paperfume.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sangyeonK on 2016-08-14.
 */
public class SharedPreferenceManager {
    private static SharedPreferenceManager ourInstance = new SharedPreferenceManager();

    public static SharedPreferenceManager getInstance() {
        return ourInstance;
    }

    private SharedPreferenceManager() {
    }

    public static final String PAPERFUME_PREFERENCES = "PAPERFUME_PREFERENCES" ;

    private SharedPreferences mSharedPreferences;

    public void init(Context context){
        mSharedPreferences = context.getSharedPreferences(PAPERFUME_PREFERENCES, Context.MODE_PRIVATE);

    }

    public void setBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, -1);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }


}
