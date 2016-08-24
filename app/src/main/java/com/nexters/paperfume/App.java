package com.nexters.paperfume;

import android.app.Application;

/**
 * Created by sangyeonK on 2016-08-24.
 *
 * 전역으로 사용되는 application객체 를 제공하는 클래스
 */
public class App extends Application{
    private static App ourInstance = new App();

    public static App getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }
}
