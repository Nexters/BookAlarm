package com.nexters.paperfume;

import android.view.View;

/**
 * Created by user on 2016-07-30.
 */

public interface SettingListener {
    void genderSetting(View view, String gender);
    void bloodSetting(View view, String blood);
    void colorSetting(View view, String color);
    void ageSetting(View view, int age);
    void finishSetting(View view);
}
