package com.gsh.utils.android;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by damon on 6/6/15.
 */
public class AndroidUtils {


    /**
     * 将字号转换为像素
     *
     * @param context context
     * @param spValue 字号的sp
     * @return 像素
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px转换为sp
     *
     * @param context context
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将dp转换为px
     *
     * @param context  context
     * @param dipValue dp值
     * @return 像素
     */
    public static int dipValue2PxValue(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px转换为dp
     *
     * @param context context
     * @param pxValue 像素值
     * @return dp
     */
    public static int pxValue2dipValue(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取应用程序的版本名字
     *
     * @param context context
     * @return 程序的版本
     * @throws PackageManager.NameNotFoundException
     */
    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        String versionName = "";
        versionName = context.getPackageManager().getPackageInfo(
                context.getPackageName(), 0).versionName;
        return versionName;

    }

    /**
     * 获取应用程序的版本号
     *
     * @param context context
     * @return 程序的versionCode
     * @throws PackageManager.NameNotFoundException
     */
    public static int getVersionCode(Context context) throws PackageManager.NameNotFoundException {
        int versionCode = 0;
        versionCode = context.getPackageManager().getPackageInfo(
                context.getPackageName(), 0).versionCode;
        return versionCode;

    }

    /**
     * 获取状态栏的高度
     *
     * @param activity activity
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * @param activity           activity
     * @param view               显示键盘的view
     * @param isModifyInputModel 是否键盘自适应
     */
    public static void openInputMethod(Activity activity, View view,
                                       boolean isModifyInputModel) {
        InputMethodManager inputMethodMgr = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodMgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        if (isModifyInputModel) {
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    /**
     * @param activity           activity
     * @param isModifyInputModel 是否键盘自适应
     */
    public static void hideInputMethod(Activity activity,
                                       boolean isModifyInputModel) {
        InputMethodManager inputMethodMgr = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodMgr.isActive()
                && activity.getCurrentFocus() != null) {
            IBinder ib = activity.getCurrentFocus()
                    .getWindowToken();
            if (ib != null) {
                inputMethodMgr.hideSoftInputFromWindow(ib,
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if (isModifyInputModel) {
                    activity.getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                }
            }
        }
    }

    /**
     * 上次点击的时间
     */
    private static long lastClickTime;

    /**
     * 防止多次点击
     *
     * @return 是否是多次点击
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        if (0 < timeD && timeD < 600) {
            return true;
        }
        return false;
    }

    public static boolean isFastDoubleClick(int delta) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
//        Log.d("button_onclick-->>","timeD-->>"+timeD+", time-->>"+time+", lastClickTime-->>"+lastClickTime);
        lastClickTime = time;
        if (0 < timeD && timeD < delta) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否需要隐藏输入框
     *
     * @param v     view
     * @param event 触摸事件
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
//获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
// 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
