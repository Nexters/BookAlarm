package com.paperfume.nexters.paperfume;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Youngdo on 2016-07-10.
 */
public class Splash extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        android.os.Handler handler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                finish();
            }
        };

        handler.sendEmptyMessageDelayed(0, 3000);
    }
}
