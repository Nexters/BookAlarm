package com.nexters.paperfume;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by user on 2016-07-24.
 */

public class GenderSettingFragment extends Fragment{
    View view;
    RadioGroup radioGroup;
    Button button;
    SettingListener mCallback;
    private String gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_gender, container, false);
        radioGroup = (RadioGroup)view.findViewById(R.id.setting_gender_group);
        button = (Button)view.findViewById(R.id.setting_gender_button);
        Log.e("View.INVISIBLE",String.valueOf(View.INVISIBLE));
        button.setVisibility(view.INVISIBLE);

        radioGroup.setOnCheckedChangeListener(radioGroup_Listner);
        button.setOnClickListener(button_Listner);

        return view;
    }

    Button.OnClickListener button_Listner = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.setting_gender_male:
                    gender = "Male";
                    break;
                case R.id.setting_gender_female:
                    gender = "Female";
                    break;
            }
            mCallback.genderSetting(view,gender);
        }
    };

    RadioGroup.OnCheckedChangeListener radioGroup_Listner = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i != -1 ){
                Log.e("View.VISIBLE",String.valueOf(View.VISIBLE));
                button.setVisibility(view.VISIBLE);
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (SettingListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
