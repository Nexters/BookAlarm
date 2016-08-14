package com.nexters.paperfume;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.nexters.paperfume.tmp.Setting;

/**
 * Created by Youngdo on 2016-07-10.
 */
public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        android.os.Handler handler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                SharedPreferences preferences = getSharedPreferences("paperfume",MODE_PRIVATE);
                Gson gson = new Gson();
                String json = preferences.getString("setting", "");
                Log.e("Settings : ",json);
                Setting setting = gson.fromJson(json,Setting.class);

                Intent intent;
                if(setting!=null){
                    intent = new Intent(Splash.this,FeelingActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(Splash.this,SettingActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 3000);
    }
}
