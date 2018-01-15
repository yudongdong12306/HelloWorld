package com.detect.detect.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class LatestTestPointSP {
    private static final String SHARED_NAME_LATEST_DETECT_POINT = "latest_detect_point";
    private static final String SHARED_KEY_LATEST_DETECT_POINT = "detect_point";
    private static Context sContext;
    private static LatestTestPointSP sSharedPrefHelper;
    private SharedPreferences preferences;

    private LatestTestPointSP() {
        preferences = sContext.getSharedPreferences(SHARED_NAME_LATEST_DETECT_POINT, Context.MODE_PRIVATE);
    }


    /**
     * 初始化
     *
     * @param context 全局的context，在Application中进行初始化
     */
    public static void initialize(Context context) {
        sContext = context;
    }


    public static LatestTestPointSP getInstance() {
        if ((sContext == null)) {
            throw new IllegalArgumentException(TAG + "必须先调用initialize方法初始化为全局的Context对象");
        }
        if (null == sSharedPrefHelper) {
            synchronized (LatestTestPointSP.class) {
                if (null == sSharedPrefHelper) {
                    sSharedPrefHelper = new LatestTestPointSP();
                }
            }
        }
        return sSharedPrefHelper;
    }

    public void setLatestDetectPoint(TestPoint testPoint) {
        if (testPoint == null) {
            return;
        }
        String jsonString = JSON.toJSONString(testPoint);
        if (TextUtils.isEmpty(jsonString)) {
            return;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_KEY_LATEST_DETECT_POINT, jsonString);
        editor.apply();
    }

    public TestPoint getLatestDetectPoint() {
        String jsonString = preferences.getString(SHARED_KEY_LATEST_DETECT_POINT, "");
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        return JSON.parseObject(jsonString, TestPoint.class);
    }
}
