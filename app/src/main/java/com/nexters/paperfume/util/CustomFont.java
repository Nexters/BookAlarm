package com.nexters.paperfume.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.nexters.paperfume.App;

/**
 * Created by nexon on 2016-08-14.
 */
public class CustomFont {
    private static CustomFont ourInstance = new CustomFont();

    public static CustomFont getInstance() {
        return ourInstance;
    }

    private CustomFont() {
        mTypeface = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/KoPubDotumLight.ttf");
    }


    Typeface mTypeface;

    public Typeface getTypeface() {
        return mTypeface;
    }
}
