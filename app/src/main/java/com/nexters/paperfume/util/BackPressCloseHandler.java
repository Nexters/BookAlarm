package com.nexters.paperfume.util;

/**
 * Created by user on 2016-08-14.
 */

import android.app.Activity;
import android.widget.Toast;

import com.nexters.paperfume.R;

public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();
            ProcessHelper.Exit();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, R.string.exit_toast, Toast.LENGTH_SHORT);
        toast.show();
    }
}