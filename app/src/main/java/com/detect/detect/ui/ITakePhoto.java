package com.detect.detect.ui;

import android.app.Activity;
import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by dongdong.yu on 18/1/11.
 */

public interface ITakePhoto {
    /**
     * 初始化PopupView
     */
    void initPopupView();

    /**
     * 显示头像
     *
     * @param bitmap 头像bitmap
     */
    void showHead(Bitmap bitmap);

    /**
     * 显示PopupView
     */
    void showPopupView();

    /**
     * 隐藏PopupView
     */
    void dismissPopupView();

    /**
     * 返回popupView是否显示
     *
     * @return popupView是否显示
     */
    boolean popupIsShowing();

    /**
     * 前往设置头像界面
     *
     * @param path 图片路径
     */
    void gotoHeadSettingActivity(String path);

    /**
     * 前往系统图库界面
     *
     * @param requestCode requestCode
     */
    void gotoSystemPhoto(int requestCode);

    /**
     * 前往系统相机界面
     *
     * @param tempFile    相片存储文件
     * @param requestCode requestCode
     */
    void gotoSystemCamera(File tempFile, int requestCode);

    /**
     * 获取当前fragment所在的Activity
     *
     * @return Activity
     */
    Activity getActivity();
}
