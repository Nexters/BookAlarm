package com.nexters.paperfume;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by user on 2016-07-27.
 */

public class PerfumeActivity extends AppCompatActivity {
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfume);
        button = (Button) findViewById(R.id.getting_books);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Selected feelings post to server

                //Intent intent = new Intent(PerfumeActivity.this, PerfumeActivity.class);
                //intent.putExtra() //books data
                //startActivity(intent);
            }
        });
    }
}