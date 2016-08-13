package com.nexters.paperfume.content.fragrance;

import com.nexters.paperfume.enums.PartOfDay;

import java.util.LinkedList;

/**
 * Created by sangyeonK on 2016-08-13.
 */


public class FragranceInfo {
    private String mName;
    private PartOfDay mPartOfDay;
    private String mImageAsset;

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


