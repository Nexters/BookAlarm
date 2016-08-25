package com.nexters.paperfume.firebase;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

/**
 * Created by sangyeonK on 2016-07-18.
 */

public class Firebase {
    private static Firebase mInstance = null;
    private static final String TAG = "Firebase";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser = null;
    private boolean mLoggedIn = false;

    public static Firebase getInstance(){
        if(mInstance == null){
            mInstance = new Firebase();
        }
        return mInstance;
    }

    public String getUserUid() {
        if(mUser == null)
            return null;
        else
            return mUser.getUid();
    }

    public boolean isLoggedIn() {
        return mLoggedIn;
    }

    private Firebase() {
        // firebaseAuth 초기화
        mAuth = FirebaseAuth.getInstance();

        // authListener 생성
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // 로그인
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
                } else {
                    // 로그아웃
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        //listener 추가
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void login(final Runnable success, final Runnable failed) {
        if(mLoggedIn) {
            if(success != null)
                success.run();
        }
        else {
            mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    mLoggedIn = true;
                    if (success != null)
                        success.run();

                    Log.d(TAG, "signInAnonymously onSuccess");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mLoggedIn = false;
                    if (failed != null)
                        failed.run();

                    Log.w(TAG, "signInAnonymously", e);
                }
            });
        }
    }
}
