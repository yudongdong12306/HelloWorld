package com.detect.detect.db;

import android.os.Environment;

import java.io.File;

/**
 * Created by dongdong.yu on 6/8/15.
 * 文件路径的常量
 */
public class FileConstant {

    /**
     * SD卡的根路径
     */
    public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * 程序的名字
     */
    private static final String APP_NAME = "detect_project";

    /**
     * 本工程所有数据存储的根路径
     */
    public static final String ROOT_PATH = SD_PATH + File.separator + APP_NAME;

    /**
     * 数据库存储的根路径
     */
    public static final String DB_PATH = ROOT_PATH + File.separator + "db" + File.separator;
    /**
     * 导出文件的根路径
     */
    public static final String OUT_PUT_PATH = ROOT_PATH + File.separator + "out_put" + File.separator;
}
