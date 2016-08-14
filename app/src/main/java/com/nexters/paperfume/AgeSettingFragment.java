package com.nexters.paperfume;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

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
        button.setVisibility(View.INVISIBLE);

        editText = (EditText)view.findViewById(R.id.setting_age_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                String getEdit = editText.getText().toString();

                if(getEdit.getBytes().length <= 0) {//빈값이 넘어올때의 처리
                    button.setVisibility(View.INVISIBLE);
                }else{
                    button.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });
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
