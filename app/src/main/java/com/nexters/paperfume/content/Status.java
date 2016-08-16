package com.nexters.paperfume.content;

import com.nexters.paperfume.enums.Feeling;

/**
 * Created by sangyeonK on 2016-08-16.
 */

public class Status {
    private static Status ourInstance = new Status();

    public static Status getInstance() {
        return ourInstance;
    }

    private Status() {

    }

    Feeling mCurrentFeeling;

    public Feeling getCurrentFeeling() {
        return mCurrentFeeling;
    }

    public void setCurrentFeeling(Feeling currentFeeling) {
        mCurrentFeeling = currentFeeling;
    }
}
