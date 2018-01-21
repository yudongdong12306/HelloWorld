//package com.detect.detect.shared_preferences;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.text.TextUtils;
//
//import com.detect.detect.utils.MD5Utils;
//
//import static android.content.ContentValues.TAG;
//
///**
// * Created by Administrator on 2018/1/19 0019.
// */
//
//public class ProjectNameMD5ValueSP {
//
//    private static final String SHARED_NAME_PROJECT_NAME_MD5 = "project_name_md5";
//    private static Context sContext;
//    private static ProjectNameMD5ValueSP sSharedPrefHelper;
//    private SharedPreferences preferences;
//
//    private ProjectNameMD5ValueSP() {
//        preferences = sContext.getSharedPreferences(SHARED_NAME_PROJECT_NAME_MD5, Context.MODE_PRIVATE);
//    }
//
//    /**
//     * 初始化
//     *
//     * @param context 全局的context，在Application中进行初始化
//     */
//    public static void initialize(Context context) {
//        sContext = context;
//    }
//
//
//    public static ProjectNameMD5ValueSP getInstance() {
//        if ((sContext == null)) {
//            throw new IllegalArgumentException(TAG + "必须先调用initialize方法初始化为全局的Context对象");
//        }
//        if (null == sSharedPrefHelper) {
//            synchronized (ProjectNameMD5ValueSP.class) {
//                if (null == sSharedPrefHelper) {
//                    sSharedPrefHelper = new ProjectNameMD5ValueSP();
//                }
//            }
//        }
//        return sSharedPrefHelper;
//    }
//
//
//    public void setProjectNameMd5(String projectName) {
//        if (TextUtils.isEmpty(projectName)) {
//            return;
//        }
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(projectName, MD5Utils.getMD5(projectName));
//        editor.putString(MD5Utils.getMD5(projectName), projectName);
//        editor.apply();
//    }
//
//    public String getProjectNameFromMd5(String md5) {
//        return preferences.getString(md5, "");
//    }
//
//    public String getMd5FromProjectName(String projectName) {
//        return preferences.getString(projectName, "");
//    }
//}
