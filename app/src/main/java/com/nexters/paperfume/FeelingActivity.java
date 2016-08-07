package com.nexters.paperfume;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Random;

/**
 * Created by user on 2016-07-27.
 */

public class FeelingActivity extends AppCompatActivity {
    Button button;
    RadioGroup radioGroup;
    private String feeling;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling);
        button = (Button) findViewById(R.id.feeling_button);
        radioGroup = (RadioGroup) findViewById(R.id.feeling_group);

        //라디오버튼에 랜덤 텍스트 입력
        {
            Random r = new Random();
            RadioButton radioButton = null;
            String[] sentences = null;
            int index;

            for(int i = 0 ; i < 4 ; i++)
            {
                switch(i) {
                    case 0:
                        radioButton = (RadioButton) findViewById(R.id.feeling_happy);
                        sentences = getResources().getStringArray(R.array.feeling_happy);
                        break;
                    case 1:
                        radioButton = (RadioButton) findViewById(R.id.feeling_miss);
                        sentences = getResources().getStringArray(R.array.feeling_miss);
                        break;
                    case 2:
                        radioButton = (RadioButton) findViewById(R.id.feeling_groomy);
                        sentences = getResources().getStringArray(R.array.feeling_groomy);
                        break;
                    case 3:
                        radioButton = (RadioButton) findViewById(R.id.feeling_stifled);
                        sentences = getResources().getStringArray(R.array.feeling_stifled);
                        break;
                }

                if(radioButton != null) {
                    index = r.nextInt(sentences.length);
                    radioButton.setText(sentences[index]);
                }
            }
        }

        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.feeling_groomy:
                        feeling = "groomy";
                        break;
                    case R.id.feeling_happy:
                        feeling = "happy";
                        break;
                    case R.id.feeling_miss:
                        feeling = "miss";
                        break;
                    case R.id.feeling_stifled:
                        feeling = "stifled";
                        break;
                }

                //Selected feelings post to server

                Intent intent = new Intent(FeelingActivity.this, PerfumeActivity.class);
                //intent.putExtra() //Perpume data
                startActivity(intent);
            }
        });
    }
}
