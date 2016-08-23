package com.nexters.paperfume;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nexters.paperfume.content.Status;
import com.nexters.paperfume.enums.Feeling;
import com.nexters.paperfume.util.CustomFont;

import java.util.ArrayList;

/**
 * Created by user on 2016-07-24.
 */

public class BloodSettingFragment extends Fragment{
    SettingListener mCallback;
    Button button;
    RadioGroup radioGroup;
    ArrayList<RadioButton> mRadioButtons = new ArrayList<RadioButton>();
    private  String blood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_blood, container, false);
        button = (Button)view.findViewById(R.id.setting_blood_button);
        radioGroup = (RadioGroup) view.findViewById(R.id.setting_blood_group);
        Log.d("View.INVISIBLE",String.valueOf(View.INVISIBLE));
        button.setVisibility(View.INVISIBLE);

        //폰트 설정, 버튼들 하드코딩...(RadioGroup 이 child layout 처리를 못해줌 )
        {
            RadioButton radioButton = null;

            for(int i = 0 ; i < 4 ; i++)
            {
                switch(i) {
                    case 0:
                        radioButton = (RadioButton) view.findViewById(R.id.blood_A);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(RadioButton bt : mRadioButtons) {
                                    bt.setChecked(false);
                                }
                                blood = "A";
                                ((RadioButton)view).setChecked(true);
                                button.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 1:
                        radioButton = (RadioButton) view.findViewById(R.id.blood_B);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(RadioButton bt : mRadioButtons) {
                                    bt.setChecked(false);
                                }
                                blood = "B";
                                ((RadioButton)view).setChecked(true);
                                button.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 2:
                        radioButton = (RadioButton) view.findViewById(R.id.blood_O);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(RadioButton bt : mRadioButtons) {
                                    bt.setChecked(false);
                                }
                                blood = "O";
                                ((RadioButton)view).setChecked(true);
                                button.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 3:
                        radioButton = (RadioButton) view.findViewById(R.id.blood_AB);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(RadioButton bt : mRadioButtons) {
                                    bt.setChecked(false);
                                }
                                blood = "AB";
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
                mCallback.bloodSetting(view,blood);
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
