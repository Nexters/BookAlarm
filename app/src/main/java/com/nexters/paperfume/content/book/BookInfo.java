package com.nexters.paperfume.content.book;

import com.nexters.paperfume.enums.Feeling;

/**
 * Created by sangyeonK on 2016-08-16.
 */

public class BookInfo {

    int mBookID = -1;
    Feeling mFeeling;
    String mTitle;
    String mAuthor;
    String mImage;
    String mInside;

    boolean mLoadedBookData = false;

    public BookInfo() {
    }

    public int getBookID() {
        return mBookID;
    }

    public void setBookID(int bookID) {
        this.mBookID = bookID;
    }

    public Feeling getFeeling() {
        return mFeeling;
    }

    public void setFeeling(Feeling feeling) {
        this.mFeeling = feeling;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getInside() {
        return mInside;
    }

    public void setInside(String inside) {
        this.mInside = inside;
    }


    public boolean isLoadedBookData() {
        return mLoadedBookData;
    }

    public void setLoadedBookData(boolean loadedBookData) {
        mLoadedBookData = loadedBookData;
    }
}