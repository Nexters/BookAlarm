package com.nexters.paperfume;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

/**
 * Created by sangyeonK on 2016-08-24.
 *
 * 전역으로 사용되는 application객체 를 제공하는 클래스
 */
public class App extends Application{
    private static App ourInstance = new App();

    public static App getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }

    public void startSplashActivity(Activity currentActivity) {
        Intent intent = new Intent(currentActivity, Splash.class);
        startActivity(intent);
    }

    /**
     * FeelingActivity 를 실행하는 notification 설정
     */
    public void notifyToRunFeelingActivity() {
        Intent intent = new Intent(this, FeelingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setWhen(System.currentTimeMillis() + 100000);
        notificationBuilder.setSmallIcon(R.mipmap.mdpiic_launcher_a_p_p);
        notificationBuilder.setContentTitle(getResources().getString(R.string.notify_title));
        notificationBuilder.setContentText(getResources().getString(R.string.notify_message));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
