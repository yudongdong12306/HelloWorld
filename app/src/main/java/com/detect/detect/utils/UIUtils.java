package com.detect.detect.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.detect.detect.R;
import com.detect.detect.global.GlobalApplication;
import com.detect.detect.ui.BaseActivity;

/**
 * 获取一些公用的参数
 */
public class UIUtils {


    public static Context getContext() {
        return GlobalApplication.getContext();
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }


    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }


//    /**
//     * TOAST
//     * @param string
//     */
//    public static void showToast(String string) {
//        showToast(string,false);
////        Toast.makeText(UIUtils.getContext(), string, Toast.LENGTH_LONG).show();
//    }

//    public static void showToast(String string, boolean isTrue) {
//        ToastUtil.getInstance(AppManager.getInstance().currentActivity(), string, isTrue).show();
////        Toast.makeText(UIUtils.getContext(), string, Toast.LENGTH_LONG).show();
//    }

//    public static String getKey(){
//        return DateUtils.currentTime()+"/"+ System.currentTimeMillis();
//    }


//    /**
//     * 日志
//     * @param s
//     */
//    public static void showTag(String s) {
//        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
//        String tag = getDefaultTag(stackTraceElement);
//        if (TextUtil.isEmpty(s)) {
//            L.e(tag+"="+"" + s);
//            return;
//        }
//        if (s.length() > 3000) {
//            for (int i = 0; i < s.length(); i += 3000) {
//                if (i + 3000 < s.length())
//                    L.e(tag+"="+s.substring(i, i + 3000));
//                else
//                    L.e(tag+"="+s.substring(i, s.length()));
//            }
//        } else {
//            L.e(tag+"="+"" + s);
//        }
//    }
//
//    /**
//     * 获取默认的TAG名称.
//     * 比如在MainActivity.java中调用了日志输出.
//     * 则TAG为MainActivity
//     */
//    private static String getDefaultTag(StackTraceElement stackTraceElement) {
//        String fileName = stackTraceElement.getFileName();
//        String stringArray[] = fileName.split("\\.");
//        String tag = stringArray[0];
//        return tag;
//    }


    /**
     * 没有动画的页面跳转
     *
     * @param activity
     * @param bundle
     */
    public static void intentActivity(Class<? extends BaseActivity> activity, Bundle bundle) {

        Intent intent = new Intent(UIUtils.getContext(), activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UIUtils.getContext().startActivity(intent);
    }

    /**
     * 没有动画的页面跳转
     *
     * @param activity
     * @param bundle
     */
    public static void intentActivityNotFlag(Class<? extends BaseActivity> activity, Bundle bundle) {

        Intent intent = new Intent(UIUtils.getContext(), activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        UIUtils.getContext().startActivity(intent);
    }

    /**
     * 右进 左出
     *
     * @param activity
     * @param bundle
     * @param mActivity
     */
    public static void intentActivity(Class<? extends BaseActivity> activity, Bundle bundle, Activity mActivity) {
        Intent intent = new Intent(UIUtils.getContext(), activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
//
//    /**
//     * 淡进入
//     * @param mActivity
//     */
//    public static void intentMainActivity(Activity mActivity) {
//        Intent intent = new Intent(UIUtils.getContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
//        mActivity.startActivity(intent);
////        mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//    }


//    /**
//     * 登陆页面
//     * 下面进来
//     * @param mActivity
//     */
//    public static void intentLoaging(Activity mActivity) {
//        Intent intent = new Intent(mActivity, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mActivity.startActivity(intent);
//        mActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.fade_in);
//    }
//
//    public static void intentLoaging(Activity mActivity, int i) {
//        Intent intent = new Intent(mActivity, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("type",i);//是否直接回到main
//        mActivity.startActivity(intent);
//        mActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.fade_in);
//    }

//    /**
//     * 查看大图
//     *
//     * @param bundle
//     * @param mActivity
//     */
//    public static void intentImageActivity(Bundle bundle, Activity mActivity) {
//        Intent intent = new Intent(UIUtils.getContext(), SeeImageActivity.class);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        mActivity.startActivity(intent);
//        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//    }


}