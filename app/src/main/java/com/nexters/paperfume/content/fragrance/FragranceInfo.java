package com.nexters.paperfume.content.fragrance;

import com.nexters.paperfume.enums.PartOfDay;

import java.util.LinkedList;

/**
 * Created by sangyeonK on 2016-08-13.
 */


public class FragranceInfo {
    private String mAdjective;          //형용사
    private String mName;               //향기 이름
    private PartOfDay mPartOfDay;       //향기 추천대 시간
    private String mImageAsset;         //이미지 어셋 위치


    public FragranceInfo() {
    }

    public String getAdjective() {
        return mAdjective;
    }

    public void setAdjective(String mAdjective) {
        this.mAdjective = mAdjective;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public PartOfDay getPartOfDay() {
        return mPartOfDay;
    }

    public void setPartOfDay(PartOfDay mPartOfDay) {
        this.mPartOfDay = mPartOfDay;
    }

    public String getImageAsset() { return mImageAsset; }

    public void setImageAsset(String mImageAsset) {
        this.mImageAsset = mImageAsset;
    }

}


