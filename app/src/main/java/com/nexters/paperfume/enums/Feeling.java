package com.nexters.paperfume.enums;

/**
 * Created by sangyeonK on 2016-08-13.
 */

public enum Feeling {
    HAPPY,      //행복해요
    MISS,       //그리워요
    GROOMY,     //우울해요
    STIFLED;    //답답해요


    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public String toMeans() {
        switch (this) {
            case HAPPY:
                return "행복한";
            case MISS:
                return "그리워하는";
            case GROOMY:
                return "우울한";
            case STIFLED:
                return "답답한";
        }
        return null;
    }
}
