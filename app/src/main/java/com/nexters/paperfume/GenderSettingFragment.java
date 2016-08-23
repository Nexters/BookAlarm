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

import com.nexters.paperfume.util.CustomFont;

import java.util.ArrayList;

/**
 * Created by user on 2016-07-24.
 */

public class GenderSettingFragment extends Fragment{
    View view;
    RadioGroup radioGroup;
    Button button;
    SettingListener mCallback;
    ArrayList<RadioButton> mRadioButtons = new ArrayList<RadioButton>();
    private String gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_gender, container, false);
        button = (Button)view.findViewById(R.id.setting_gender_button);
        Log.d("View.INVISIBLE",String.valueOf(View.INVISIBLE));
        button.setVisibility(view.INVISIBLE);

        //폰트 설정, 버튼들 하드코딩...(RadioGroup 이 child layout 처리를 못해줌 )
        {
            RadioButton radioButton = null;

            for(int i = 0 ; i < 2 ; i++)
            {
                switch(i) {
                    case 0:
                        radioButton = (RadioButton) view.findViewById(R.id.setting_gender_male);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(RadioButton bt : mRadioButtons) {
                                    bt.setChecked(false);
                                }
                                gender = "남성";
                                ((RadioButton)view).setChecked(true);
                                button.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 1:
                        radioButton = (RadioButton) view.findViewById(R.id.setting_gender_female);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(RadioButton bt : mRadioButtons) {
                                    bt.setChecked(false);
                                }
                                gender = "여성";
                                ((RadioButton)view).setChecked(true);
                                button.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                }

                if(radioButton != null) {
                    radioButton.setTypeface(CustomFont.getInstance().getTypeface());
                    mRadioButtons.add(radioButton);
                }
            }
        }

        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
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
