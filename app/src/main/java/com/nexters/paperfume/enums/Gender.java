package com.nexters.paperfume.enums;

/**
 * Created by user on 2016-08-24.
 */
public enum Gender {
    MALE,      //남성
    FEMALE;       //여성


    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public String toMeans() {
        switch (this) {
            case MALE:
                return "남성";
            case FEMALE:
                return "여성";
        }
        return null;
    }
}
