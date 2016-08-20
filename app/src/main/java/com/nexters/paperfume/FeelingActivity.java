package com.nexters.paperfume;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nexters.paperfume.content.Status;
import com.nexters.paperfume.enums.Feeling;

import com.nexters.paperfume.util.CustomFont;
import com.nexters.paperfume.util.BackPressCloseHandler;

import java.util.ArrayList;


/**
 * Created by user on 2016-07-27.
 */

public class FeelingActivity extends AppCompatActivity {
    Button button;
    RadioGroup radioGroup;
    ArrayList<RadioButton> mRadioButtons = new ArrayList<RadioButton>();
    private Feeling feeling;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling);
        button = (Button) findViewById(R.id.feeling_button);
        button.setVisibility(View.INVISIBLE);
        radioGroup = (RadioGroup) findViewById(R.id.feeling_group);
        radioGroup.setOnCheckedChangeListener(radioGroup_Listner);
        backPressCloseHandler = new BackPressCloseHandler(this);

        //폰트 설정, 버튼들 하드코딩...(RadioGroup 이 child layout 처리를 못해줌 )
        {
            RadioButton radioButton = null;

            for(int i = 0 ; i < 4 ; i++)
            {
                switch(i) {
                    case 0:
                        radioButton = (RadioButton) findViewById(R.id.feeling_happy);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(RadioButton bt : mRadioButtons) {
                                    bt.setChecked(false);
                                }
                                feeling = Feeling.HAPPY;
                                ((RadioButton)view).setChecked(true);
                                button.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 1:
                        radioButton = (RadioButton) findViewById(R.id.feeling_miss);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(RadioButton bt : mRadioButtons) {
                                    bt.setChecked(false);
                                }
                                feeling = Feeling.MISS;
                                ((RadioButton)view).setChecked(true);
                                button.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 2:
                        radioButton = (RadioButton) findViewById(R.id.feeling_groomy);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(RadioButton bt : mRadioButtons) {
                                    bt.setChecked(false);
                                }
                                feeling = Feeling.GROOMY;
                                ((RadioButton)view).setChecked(true);
                                button.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 3:
                        radioButton = (RadioButton) findViewById(R.id.feeling_stifled);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for(RadioButton bt : mRadioButtons) {
                                    bt.setChecked(false);
                                }
                                feeling = Feeling.STIFLED;
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

                //Selected feelings post to server

                Intent intent = new Intent(FeelingActivity.this, PerfumeActivity.class);
                Status.getInstance().setCurrentFeeling(feeling);
                startActivity(intent);
            }
        });
    }

    RadioGroup.OnCheckedChangeListener radioGroup_Listner = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i != -1 ){
                button.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void backButtonClick(View view){
        backPressCloseHandler.onBackPressed();
    }
}
