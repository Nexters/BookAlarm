package com.nexters.paperfume;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by user on 2016-07-24.
 */

public class AgeSettingFragment extends Fragment {
    SettingListener mCallback;
    Button button;
    EditText editText;
    private int age;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_age, container, false);
        button = (Button)view.findViewById(R.id.setting_age_button);
        editText = (EditText)view.findViewById(R.id.setting_age_edit);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                int age = Integer.parseInt(editText.getText().toString());
                mCallback.ageSetting(view, age);
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
