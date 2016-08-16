package com.nexters.paperfume.content.book;

import com.nexters.paperfume.enums.Feeling;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sangyeonK on 2016-08-14.
 */
public class FeaturedBook {
    private static FeaturedBook ourInstance = new FeaturedBook();

    public static FeaturedBook getInstance() {
        return ourInstance;
    }

    private FeaturedBook() {
        happy = new ArrayList<Integer>();
        miss = new ArrayList<Integer>();
        groomy = new ArrayList<Integer>();
        stifled = new ArrayList<Integer>();
    }

    private ArrayList<Integer> happy;
    private ArrayList<Integer> miss;
    private ArrayList<Integer> groomy;
    private ArrayList<Integer> stifled;

    public ArrayList<Integer> getHappy() {
        return happy;
    }

    public void setHappy(ArrayList<Integer> happy) {
        this.happy = happy;
    }

    public ArrayList<Integer> getMiss() {
        return miss;
    }

    public void setMiss(ArrayList<Integer> miss) {
        this.miss = miss;
    }

    public ArrayList<Integer> getGroomy() {
        return groomy;
    }

    public void setGroomy(ArrayList<Integer> groomy) {
        this.groomy = groomy;
    }

    public ArrayList<Integer> getStifled() {
        return stifled;
    }

    public void setStifled(ArrayList<Integer> stifled) {
        this.stifled = stifled;
    }

    /**
     * 기분별 추천책 데이터를 가져온다.
     * @param feeling 기분
     * @return 추천책 데이터
     */
    public ArrayList<Integer> getFeaturedBooks(Feeling feeling) {
        switch (feeling) {
            case HAPPY:
                return this.happy;
            case MISS:
                return this.miss;
            case GROOMY:
                return this.groomy;
            case STIFLED:
                return this.stifled;
            default:
                return null;
        }
    }

    public void shuffle() {
        Random r = new Random();

        for (int i = 0; i < 4; i++) {
            ArrayList<Integer> list;
            if (i == 0) list = happy;
            else if (i == 1) list = miss;
            else if (i == 2) list = groomy;
            else list = stifled;

            for (int j = list.size() - 1; j > 0; j--) {
                int index = r.nextInt(j + 1);

                //simple swap value
                int a = happy.get(index);
                int b = happy.get(j);

                happy.set(j, a);
                happy.set(index, b);
            }
        }
    }
}
