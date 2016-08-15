package com.nexters.paperfume;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.nexters.paperfume.content.Setting;
import com.nexters.paperfume.util.SharedPreferenceManager;

/**
 * Created by user on 2016-07-24.
 */

public class SettingActivity extends AppCompatActivity implements SettingListener{

    public static final String KEY_SETTING = "SETTING";

    Setting setting;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        if(savedInstanceState==null){
            fragmentTransaction(new GenderSettingFragment());
        }
        setting = Setting.getInstance();
    }

    @Override
    public void genderSetting(View view,String gender) {
        Fragment fragment;
        Log.d("Gender",gender);
        setting.setGender(gender);
        fragment = new BloodSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void bloodSetting(View view, String blood) {
        Fragment fragment;
        Log.d("blood",blood);
        setting.setBlood(blood);
        fragment = new AgeSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void ageSetting(View view, int age) {
        Fragment fragment;
        Log.d("Age",String.valueOf(age));
        setting.setAge(age);
        fragment = new ColorSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void colorSetting(View view, String color) {
        Fragment fragment;
        Log.d("Color",color);
        setting.setColor(color);
        fragment = new FinishSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void finishSetting(View view) {
        Log.d("Setting", "Finished");

        Gson gson = new Gson();
        String json = gson.toJson(setting);
        Log.d("Setting : ",json );
        SharedPreferenceManager.getInstance().setString(KEY_SETTING, json);

        FragmentManager fm = getFragmentManager();
        Intent intent = new Intent(SettingActivity.this, FeelingActivity.class);
        startActivity(intent);
        finish();
    }

    public void fragmentTransaction(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.setting, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void backButtonClick(View view){
        onBackPressed();
    }
}
