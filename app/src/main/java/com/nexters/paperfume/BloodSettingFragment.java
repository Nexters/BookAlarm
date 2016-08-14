package com.nexters.paperfume;

import android.app.Activity;
import android.app.Fragment;
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

/**
 * Created by user on 2016-07-24.
 */

public class BloodSettingFragment extends Fragment{
    SettingListener mCallback;
    Button button;
    RadioGroup radioGroup;
    private  String blood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_blood, container, false);
        button = (Button)view.findViewById(R.id.setting_blood_button);
        radioGroup = (RadioGroup) view.findViewById(R.id.setting_blood_group);
        Log.e("View.INVISIBLE",String.valueOf(View.INVISIBLE));
        button.setVisibility(View.INVISIBLE);

        radioGroup.setOnCheckedChangeListener(radioGroup_Listner);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.setting_blood_o:
                        blood = "O형";
                        break;
                    case R.id.setting_blood_b:
                        blood = "B형";
                        break;
                    case R.id.setting_blood_a:
                        blood = "A형";
                        break;
                    case R.id.setting_blood_ab:
                        blood = "AB형";
                        break;
                }
                mCallback.bloodSetting(view,blood);
            }
        });
        return view;
    }

    RadioGroup.OnCheckedChangeListener radioGroup_Listner = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i != -1 ){
                Log.e("View.VISIBLE",String.valueOf(View.VISIBLE));
                button.setVisibility(View.VISIBLE);
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
