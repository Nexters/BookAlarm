package com.nexters.paperfume;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Created by user on 2016-07-24.
 */

public class ColorSettingFragment extends Fragment{

    SettingListener mCallback;
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    RadioGroup radioGroup3;
    Button button;
    private boolean isChecking = true;
    private int mCheckId;
    private String color;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_color, container, false);
        radioGroup1 = (RadioGroup)view.findViewById(R.id.setting_color_group1);
        radioGroup1.setOnCheckedChangeListener(radioGroup1_Listner);
        radioGroup2 = (RadioGroup)view.findViewById(R.id.setting_color_group2);
        radioGroup2.setOnCheckedChangeListener(radioGroup2_Listner);
        radioGroup3 = (RadioGroup)view.findViewById(R.id.setting_color_group3);
        radioGroup3.setOnCheckedChangeListener(radioGroup3_Listner);
        button = (Button)view.findViewById(R.id.setting_color_button);
        button.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (mCheckId) {
                    case R.id.setting_color_black:
                        color = "검정";
                        break;
                    case R.id.setting_color_blue:
                        color = "파랑";
                        break;
                    case R.id.setting_color_green:
                        color = "초록";
                        break;
                    case R.id.setting_color_pink:
                        color = "분홍";
                        break;
                    case R.id.setting_color_purple:
                        color = "보라";
                        break;
                    case R.id.setting_color_red:
                        color = "빨강";
                        break;
                    case R.id.setting_color_sky:
                        color = "하늘";
                        break;
                    case R.id.setting_color_white:
                        color = "하양";
                        break;
                    case R.id.setting_color_yelow:
                        color = "노랑";
                        break;
                }
                mCallback.colorSetting(view,color);
            }
        });
        return view;
    }

    RadioGroup.OnCheckedChangeListener radioGroup1_Listner = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i != -1 && isChecking){
                button.setVisibility(View.VISIBLE);
                isChecking = false;
                radioGroup2.setOnCheckedChangeListener(null);
                radioGroup3.setOnCheckedChangeListener(null);
                radioGroup2.clearCheck();
                radioGroup3.clearCheck();
                radioGroup2.setOnCheckedChangeListener(radioGroup2_Listner);
                radioGroup3.setOnCheckedChangeListener(radioGroup3_Listner);
                mCheckId = i;
            }
            isChecking = true;
        }
    };

    RadioGroup.OnCheckedChangeListener radioGroup2_Listner = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i != -1 && isChecking){
                button.setVisibility(View.VISIBLE);
                isChecking = false;
                radioGroup1.setOnCheckedChangeListener(null);
                radioGroup3.setOnCheckedChangeListener(null);
                radioGroup1.clearCheck();
                radioGroup3.clearCheck();
                radioGroup1.setOnCheckedChangeListener(radioGroup1_Listner);
                radioGroup3.setOnCheckedChangeListener(radioGroup3_Listner);
                mCheckId = i;
            }
            isChecking = true;
        }
    };

    RadioGroup.OnCheckedChangeListener radioGroup3_Listner = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i != -1 && isChecking){
                button.setVisibility(View.VISIBLE);
                isChecking = false;
                radioGroup2.setOnCheckedChangeListener(null);
                radioGroup1.setOnCheckedChangeListener(null);
                radioGroup2.clearCheck();
                radioGroup1.clearCheck();
                radioGroup2.setOnCheckedChangeListener(radioGroup2_Listner);
                radioGroup1.setOnCheckedChangeListener(radioGroup1_Listner);
                mCheckId = i;
            }
            isChecking = true;
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
