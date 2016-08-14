package com.nexters.paperfume;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.nexters.paperfume.tmp.Setting;

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
        button.setVisibility(View.INVISIBLE);
        radioGroup = (RadioGroup) findViewById(R.id.feeling_group);
        radioGroup.setOnCheckedChangeListener(radioGroup_Listner);
/*        SharedPreferences preferences = getSharedPreferences("paperfume",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("setting", "");
        Log.e("Settings : ",json);*/

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

    RadioGroup.OnCheckedChangeListener radioGroup_Listner = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i != -1 ){
                button.setVisibility(View.VISIBLE);
            }
        }
    };

    public void backButtonClick(View view){
        onBackPressed();
    }
}
