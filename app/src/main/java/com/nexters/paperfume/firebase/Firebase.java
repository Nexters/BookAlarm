package com.nexters.paperfume.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by sangyeonK on 2016-07-18.
 */

public class Firebase {
    private static Firebase mInstance = null;
    private static final String TAG = "Firebase";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser = null;

    public static Firebase getInstance(){
        if(mInstance == null){
            mInstance = new Firebase();
        }
        return mInstance;
    }

    public FirebaseUser getUser() {
        return mUser;
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

    public void login() {
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInAnonymously", task.getException());
                }
            }
        });
    }

}
