package com.nexters.paperfume;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Junwoo on 2016-08-08.
 */
public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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

        android.os.Handler handler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 5000);
    }
}
