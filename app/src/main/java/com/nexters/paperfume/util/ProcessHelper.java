package com.nexters.paperfume.util;

/**
 * Created by sangyeon on 2016-08-18.
 */

public class ProcessHelper {
    public static void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
