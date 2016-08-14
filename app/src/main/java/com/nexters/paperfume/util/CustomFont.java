package com.nexters.paperfume.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by nexon on 2016-08-14.
 */
public class CustomFont {
    private static CustomFont ourInstance = new CustomFont();

    public static CustomFont getInstance() {
        return ourInstance;
    }

    private CustomFont() {
    }

    Typeface mTypeface;
    public void init(AssetManager assetManager){
        mTypeface = Typeface.createFromAsset(assetManager, "fonts/KoPubDotumLight.ttf");
    }

    public Typeface getTypeface() {
        return mTypeface;
    }
}
