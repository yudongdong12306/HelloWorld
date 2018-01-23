package com.detect.detect.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.detect.detect.utils.MD5Utils;

import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class ProjectNameUUIDSP {

    private static final String SHARED_NAME_PROJECT_NAME_MD5 = "project_name_md5";
    private static Context sContext;
    private static ProjectNameUUIDSP sSharedPrefHelper;
    private SharedPreferences preferences;

    private ProjectNameUUIDSP() {
        preferences = sContext.getSharedPreferences(SHARED_NAME_PROJECT_NAME_MD5, Context.MODE_PRIVATE);
    }

    /**
     * 初始化
     *
     * @param context 全局的context，在Application中进行初始化
     */
    public static void initialize(Context context) {
        sContext = context;
    }


    public static ProjectNameUUIDSP getInstance() {
        if ((sContext == null)) {
            throw new IllegalArgumentException(TAG + "必须先调用initialize方法初始化为全局的Context对象");
        }
        if (null == sSharedPrefHelper) {
            synchronized (ProjectNameUUIDSP.class) {
                if (null == sSharedPrefHelper) {
                    sSharedPrefHelper = new ProjectNameUUIDSP();
                }
            }
        }
        return sSharedPrefHelper;
    }


    public void setProjectNameMd5(String projectName) {
        if (TextUtils.isEmpty(projectName)) {
            return;
        }
        String uuidProject = getUUIDFromProjectName(projectName);
        if (!TextUtils.isEmpty(uuidProject)) {
            return;
        }
        SharedPreferences.Editor editor = preferences.edit();
        String uuid = "project" + UUID.randomUUID().toString().replaceAll("-", "");
        editor.putString(projectName, uuid);
        editor.putString(uuid, projectName);
        editor.apply();
    }

    public String getProjectNameFromMd5(String tableName) {
        return preferences.getString(tableName, "");
    }

    public String getUUIDFromProjectName(String projectName) {
        return preferences.getString(projectName, "");
    }
}
