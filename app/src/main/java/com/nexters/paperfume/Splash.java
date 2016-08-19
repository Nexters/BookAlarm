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
/*
        final LinearLayout backgroundOne = (LinearLayout) findViewById(R.id.splash);
        final LinearLayout backgroundTwo = (LinearLayout) findViewById(R.id.splash2);
        final RelativeLayout foreground = (RelativeLayout) findViewById(R.id.splash3);
        final TextView text1 = (TextView) findViewById(R.id.splashText);
        final TextView text2 = (TextView) findViewById(R.id.splashText2);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(80000L);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = -(width * progress);
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX + width);
            }
        });

        animator.start();

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(foreground, "alpha", .1f, 1f);
        fadeIn.setDuration(1000);
        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(text1, "alpha", .1f, 1f);
        fadeIn2.setDuration(700);
        ObjectAnimator delay = ObjectAnimator.ofFloat(text1, "alpha", 1f, 1f);
        delay.setDuration(1000);
        ObjectAnimator fadeIn3 = ObjectAnimator.ofFloat(text2, "alpha", .1f, 1f);
        fadeIn3.setDuration(700);

        final TranslateAnimation slide = new TranslateAnimation(0, 0, 0, -200);
        slide.setDuration(500);
        slide.setFillAfter(true);

        text1.setVisibility(View.INVISIBLE);
        text2.setVisibility(View.INVISIBLE);

        final AnimatorSet mAnimationSet = new AnimatorSet();
        final AnimatorSet mAnimationSet2 = new AnimatorSet();
        final AnimatorSet mDelay = new AnimatorSet();
        final AnimatorSet mAnimationSet3 = new AnimatorSet();

        mAnimationSet.play(fadeIn);
        mAnimationSet2.play(fadeIn2);
        mDelay.play(delay);
        mAnimationSet3.play(fadeIn3);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                text1.setVisibility(View.VISIBLE);
                mAnimationSet2.start();
            }
        });

        mAnimationSet2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mDelay.start();
            }
        });

        mDelay.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationEnd(animation);
                text1.startAnimation(slide);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                text2.setVisibility(View.VISIBLE);
                mAnimationSet3.start();
            }
        });

        mAnimationSet.start();
*/
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
