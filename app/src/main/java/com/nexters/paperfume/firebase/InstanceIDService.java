package com.nexters.paperfume.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by sangyeonK on 2016-07-19.
 */

public class InstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "F.InstanceIDService";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        FirebaseUser user = Firebase.getInstance().getUser();
        if(user != null) {
            //Realtime Database 연결
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(String.format("users/%s/registration_id", user.getUid()));
            ref.setValue(token);
        }
    }
}