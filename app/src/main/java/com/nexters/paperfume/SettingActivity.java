package com.nexters.paperfume;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by user on 2016-07-24.
 */

public class SettingActivity extends AppCompatActivity implements SettingListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //스플래시 액티비티 실행
        Intent intent = new Intent(SettingActivity.this, Splash.class);
        startActivity(intent);
        setContentView(R.layout.activity_setting);
        if(savedInstanceState==null){
            fragmentTransaction(new GenderSettingFragment());
        }
    }

    @Override
    public void genderSetting(View view,String gender) {
        Fragment fragment;
        Log.e("Gender",gender);
        fragment = new BloodSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void bloodSetting(View view, String blood) {
        Fragment fragment;
        Log.e("blood",blood);
        fragment = new ColorSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void colorSetting(View view, String color) {
        Fragment fragment;
        Log.e("Color",color);
        fragment = new AgeSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void ageSetting(View view, int age) {
        Fragment fragment;
        Log.e("Age",String.valueOf(age));
        fragment = new FinishSettingFragment();
        fragmentTransaction(fragment);
    }

    @Override
    public void finishSetting(View view) {
        Log.e("Setting", "Finished");
        Intent intent = new Intent(SettingActivity.this,FeelingActivity.class);
        startActivity(intent);

    }

    public void fragmentTransaction(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.setting, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*    public void selectFrag(View view){
        Fragment fr;
        Log.e("View",view.toString());

        RadioGroup radioGroup;
        RadioButton radioButton;
        if(view == findViewById(R.id.setting_gender_button)){
            radioGroup = (RadioGroup) findViewById(R.id.setting_gender_group);
            radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            fr = new BloodSettingFragment();
            Log.e("Gender",radioButton.getText().toString());

        }else if(view == findViewById(R.id.setting_blood_button)){
            fr = new AgeSettingFragment();
            Log.e("Age","selected");
        }else if(view == findViewById(R.id.setting_age_button)){
            fr = new ColorSettingFragment();
            Log.e("Color","selected");
        }else if(view == findViewById(R.id.setting_color_button)){
            fr = new FinishSettingFragment();
            Log.e("Finish","selected");
        }else{
            fr = new GenderSettingFragment();
            Log.e("Gender","selected");
            //fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //BackStack Clear
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.setting, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void selectFinish(View view){
        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Intent intent = new Intent(SettingActivity.this, FeelingActivity.class);
      ///  startActivity(intent);
    }*/
}
