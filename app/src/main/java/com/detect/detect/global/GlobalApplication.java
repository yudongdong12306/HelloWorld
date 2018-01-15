package com.detect.detect.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.detect.detect.shared_preferences.LatestTestPointSP;
import com.detect.detect.shared_preferences.ProjectInfoSP;

import java.util.prefs.Preferences;


/**
 * Created by dongdong.yu on 2018/1/8.
 * <p>
 * 全局Application
 */

public class GlobalApplication extends Application {
    private static final String LOG_TAG = "YZ_LOGGER";
    protected static Context context;
    protected static Handler handler;
    protected static int mainThreadId;
    private static GlobalApplication mApp;

    public static synchronized GlobalApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
        ProjectInfoSP.initialize(this);
        LatestTestPointSP.initialize(this);
    }

    /**
     * 获取上下文对象
     *
     * @return context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return handler;
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return mainThreadId;
    }
}
