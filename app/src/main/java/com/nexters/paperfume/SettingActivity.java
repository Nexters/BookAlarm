package com.nexters.paperfume;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.nexters.paperfume.tmp.Setting;

/**
 * Created by user on 2016-07-24.
 */

public class SettingActivity extends AppCompatActivity implements SettingListener{

    Setting setting;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
/*        //스플래시 액티비티 실행
        Intent intent = new Intent(SettingActivity.this, Splash.class);
        startActivity(intent);*/

        setContentView(R.layout.activity_setting);
        if(savedInstanceState==null){
            fragmentTransaction(new GenderSettingFragment());
        }
        setting = new Setting();
    }

    @Override
    public void genderSetting(View view,String gender) {
        Fragment fragment;
        Log.e("Gender",gender);
        setting.setGender(gender);
        fragment = new BloodSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void bloodSetting(View view, String blood) {
        Fragment fragment;
        Log.e("blood",blood);
        setting.setBlood(blood);
        fragment = new AgeSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void ageSetting(View view, int age) {
        Fragment fragment;
        Log.e("Age",String.valueOf(age));
        setting.setAge(age);
        fragment = new ColorSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void colorSetting(View view, String color) {
        Fragment fragment;
        Log.e("Color",color);
        setting.setColor(color);
        fragment = new FinishSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void finishSetting(View view) {
        Log.e("Setting", "Finished");

        SharedPreferences preferences = getSharedPreferences("paperfume",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(setting);
        Log.e("Setting : ",json );
        editor.putString("setting",json);
        editor.commit();

        FragmentManager fm = getFragmentManager();
        Intent intent = new Intent(SettingActivity.this,FeelingActivity.class);
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
