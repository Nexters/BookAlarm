package com.nexters.paperfume.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sangyeonK on 2016-08-13.
 */

public class RecommendBooks {

    private static RecommendBooks ourInstance = new RecommendBooks();

    private List<Integer> happy;
    private List<Integer> miss;
    private List<Integer> groomy;
    private List<Integer> stifled;

    public static RecommendBooks getInstance() {
        return ourInstance;
    }

    public RecommendBooks() {
        happy = new ArrayList<Integer>(10);
        miss = new ArrayList<Integer>(10);
        groomy = new ArrayList<Integer>(10);
        stifled = new ArrayList<Integer>(10);
    }

    public RecommendBooks(List<Integer> miss, List<Integer> happy, List<Integer> stifled, List<Integer> groomy) {
        this.miss = miss;
        this.happy = happy;
        this.stifled = stifled;
        this.groomy = groomy;
    }

    public List<Integer> getHappy() {
        return happy;
    }

    public void setHappy(List<Integer> happy) {
        this.happy = happy;
    }

    public List<Integer> getMiss() {
        return miss;
    }

    public void setMiss(List<Integer> miss) {
        this.miss = miss;
    }

    public List<Integer> getGroomy() {
        return groomy;
    }

    public void setGroomy(List<Integer> groomy) {
        this.groomy = groomy;
    }

    public List<Integer> getStifled() {
        return stifled;
    }

    public void setStifled(List<Integer> stifled) {
        this.stifled = stifled;
    }
}
