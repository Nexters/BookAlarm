package com.nexters.paperfume;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.nexters.paperfume.content.Setting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexters.paperfume.content.book.MyBook;
import com.nexters.paperfume.content.fragrance.FragranceManager;
import com.nexters.paperfume.content.book.FeaturedBook;
import com.nexters.paperfume.firebase.Firebase;
import com.nexters.paperfume.util.CustomFont;
import com.nexters.paperfume.util.ProcessHelper;
import com.nexters.paperfume.util.SharedPreferenceManager;

import static android.os.Process.getElapsedCpuTime;


/**
 * Created by Junwoo on 2016-08-08.
 */
public class Splash extends AppCompatActivity {
    public static final String  TAG = "SPLASH";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final long MIN_SPLASH_VIEW_TIME = 3000L;
    private static final long MAX_SPLASH_VIEW_TIME = 10000L;

    private Handler mSplashEndHander;
    private AlertDialog mLoginFailedDialog;
    private long mElapsedCpuTimeAtOnCreate;
    private boolean mRetrievedRecommendBookData = false;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Splash Activity 실행시, 글로벌하게 사용되는 객체들의 초기화를 진행한다.
         */

        //SharedPreferenceManager 초기화
        SharedPreferenceManager.getInstance().init(getApplicationContext());
        //FragranceManager 초기화
        FragranceManager.getInstance().init(getResources(), getAssets());
        //CustomFont 초기화
        CustomFont.getInstance().init(getAssets());
        //MyBook 초기화
        MyBook.getInstance().init(getApplicationContext());

        setContentView(R.layout.activity_splash);

        final ImageView loadingLogo = (ImageView) findViewById(R.id.loading_logo);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(loadingLogo,"alpha",0.0f, 1.0f);
        fadeIn.setStartDelay(1000L);
        fadeIn.setDuration(2000L);

        fadeIn.start();

        mLoginFailedDialog = new AlertDialog.Builder(Splash.this).create();
        mLoginFailedDialog.setTitle(R.string.error);
        mLoginFailedDialog.setMessage(getResources().getString(R.string.failed_login));
        mLoginFailedDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getText(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ProcessHelper.Exit();
                    }
                });

        mSplashEndHander = new android.os.Handler(){
            boolean mAlreadyCalled = false;
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if( true == mAlreadyCalled )
                    return;
                mAlreadyCalled = true;

                if(false == mRetrievedRecommendBookData) {
                    processLoginFail();
                    return;
                }

                String json = SharedPreferenceManager.getInstance().getString(SettingActivity.KEY_SETTING);

                if( json !=null){
                    Log.d("Settings : ",json);
                    Gson gson = new Gson();
                    Setting setting = gson.fromJson(json,Setting.class);
                    Setting.getInstance().loadSetting( setting );

                    Intent intent = new Intent(Splash.this,FeelingActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Splash.this,SettingActivity.class);
                    startActivity(intent);
                }

                finish();
            }

        };

        //Firebase 로그인
        Firebase.getInstance().login(
                new Runnable() {
                    @Override
                    public void run() {
                        processLoginSuccess();
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        processLoginFail();
                    }
                } );

        //OnCreate 된 시간 측정
        mElapsedCpuTimeAtOnCreate = getElapsedCpuTime();

        mSplashEndHander.sendEmptyMessageDelayed(0,MAX_SPLASH_VIEW_TIME);
    }

    private void processLoginSuccess(){
        //로그인 성공에 대한 처리
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("recommend_books/by_feeling");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        FeaturedBook rbook = dataSnapshot.getValue(FeaturedBook.class);

                        FeaturedBook.getInstance().getHappy().clear();
                        FeaturedBook.getInstance().getHappy().addAll(rbook.getHappy());

                        FeaturedBook.getInstance().getMiss().clear();
                        FeaturedBook.getInstance().getMiss().addAll(rbook.getMiss());

                        FeaturedBook.getInstance().getGroomy().clear();
                        FeaturedBook.getInstance().getGroomy().addAll(rbook.getGroomy());

                        FeaturedBook.getInstance().getStifled().clear();
                        FeaturedBook.getInstance().getStifled().addAll(rbook.getStifled());

                        FeaturedBook.getInstance().shuffle();

                        mRetrievedRecommendBookData = true;

                        if(checkPlayService()) {
                            long elapsedTime = getElapsedCpuTime() - mElapsedCpuTimeAtOnCreate;
                            long delayTime = MIN_SPLASH_VIEW_TIME - elapsedTime;
                            if(delayTime < 0)
                                mSplashEndHander.sendEmptyMessage(0);
                            else
                                mSplashEndHander.sendEmptyMessageDelayed(0, delayTime);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mLoginFailedDialog.show();
                    }
                }
        );


    }

    private void processLoginFail(){
        //로그인 실패에 대한 처리 ( 네트워크 연결 실패 )
        mLoginFailedDialog.show();
    }

    /**
     * 구글플레이서비스 가 설치되어 있어야 한다.
     * @return 설치여부 반환
     */
    private boolean checkPlayService(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                ProcessHelper.Exit();
            }
            return false;
        }
        return true;
    }
}
