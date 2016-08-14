package com.nexters.paperfume;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by user on 2016-08-14.
 */

public class LicenseActivity extends AppCompatActivity {
    TextView textView;
    ScrollView scrollView;
    InputStream in;
    InputStreamReader stream;
    BufferedReader buffer;
    StringBuilder sb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.fullScroll(View.FOCUS_UP);

        try{
            in = getResources().openRawResource(R.raw.licenses);
            if(in != null){
                stream = new InputStreamReader(in, "utf-8");
                buffer = new BufferedReader(stream);
                String read;
                sb = new StringBuilder("");

                while((read=buffer.readLine())!=null){
                    sb.append(read+"\n");
                }

                in.close();

                textView = (TextView)findViewById(R.id.license_text);
                textView.setText(sb.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
