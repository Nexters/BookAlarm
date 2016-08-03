package com.nexters.paperfume;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by user on 2016-07-24.
 */

public class GenderSettingFragment extends Fragment{
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button button;
    SettingListener mCallback;
    private String gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_gender, container, false);
        radioGroup = (RadioGroup)view.findViewById(R.id.setting_gender_group);
        button = (Button)view.findViewById(R.id.setting_gender_button);
        button.setOnClickListener(new Button.OnClickListener(){
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
        });
        return view;
    }

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
