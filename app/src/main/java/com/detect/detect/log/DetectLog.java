package com.detect.detect.log;

import android.util.Log;

/**
 * Created by yu on 18/1/13.
 */

public class DetectLog {
    private static final String TAG = "DetectLog";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void v(String msg) {
        Log.v(TAG, msg);
    }
}

