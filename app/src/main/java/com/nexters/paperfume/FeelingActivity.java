package com.nexters.paperfume;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

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
