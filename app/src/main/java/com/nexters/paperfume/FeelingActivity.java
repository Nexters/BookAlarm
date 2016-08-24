package com.nexters.paperfume;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

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
    ArrayList<RadioButton> mRadioButtons = new ArrayList<RadioButton>();
    private Feeling feeling;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling);
        button = (Button) findViewById(R.id.feeling_button);
        button.setVisibility(View.INVISIBLE);
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

        //notifyUser();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void backButtonClick(View view){
        backPressCloseHandler.onBackPressed();
    }

    /**
     * 로컬 notification 설정
     */
    public void notifyUser() {
        Intent intent = new Intent(this, Splash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(FeelingActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.mdpiic_launcher_a_p_p);
        notificationBuilder.setContentTitle("MessageTitle");
        notificationBuilder.setContentText("MessageBody");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
