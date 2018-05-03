package com.detect.detect.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;


public class CommonSP {
    private static Context sContext;
    private static CommonSP sSharedPrefHelper;
    private static final String TAG = "CommonSP";
    private static final String SHARED_NAME_COMMON_DATA = "common_data";
    private static final String SHARED_KEY_REMEMBER_CHOICE = "remember_choice";

    /**
     * 初始化
     *
     * @param context 全局的context，在Application中进行初始化
     */
    public static void initialize(Context context) {
        sContext = context;
    }

    private SharedPreferences preferences;

    private CommonSP() {
        preferences = sContext.getSharedPreferences(SHARED_NAME_COMMON_DATA, Context.MODE_PRIVATE);
    }


    public static CommonSP getInstance() {
        if ((sContext == null)) {
            throw new IllegalArgumentException(TAG + "必须先调用initialize方法初始化为全局的Context对象");
        }
        if (null == sSharedPrefHelper) {
            synchronized (LatestTestPointSP.class) {
                if (null == sSharedPrefHelper) {
                    sSharedPrefHelper = new CommonSP();
                }
            }
        }
        return sSharedPrefHelper;
    }

    public void setRememberChoice() {
        preferences.edit().putBoolean(SHARED_KEY_REMEMBER_CHOICE, true).apply();
    }

    public boolean getRememberChoice() {
        return preferences.getBoolean(SHARED_KEY_REMEMBER_CHOICE, false);
    }
}
