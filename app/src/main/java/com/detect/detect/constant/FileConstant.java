package com.detect.detect.constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by damon on 6/8/15.
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
    private static final String APP_NAME = "pudding";

    /**
     * 布丁所有数据存储的根路径
     */
    public static final String ROOT_PATH = SD_PATH + File.separator + APP_NAME;

    /**
     * log存储的根路径
     */
    public static final String LOG_PATH = ROOT_PATH + File.separator + "log";
    /**
     * 数据库存储的根路径
     */
    public static final String DB_PATH = ROOT_PATH + File.separator + "db" + File.separator;
    /**
     * SDPATH_RES: 存放图片等资源文件 *
     */
    public static final String RES_PATH = ROOT_PATH + File.separator + "res" + File.separator;
    /**
     * APP_UPDATE_PATH: APP升级信息保存的路径
     */
    public static final String APP_UPDATE_PATH = ROOT_PATH + File.separator + "appupdate" + File.separator;
    /**
     * APP_CONFIG_DIR:系统配置信息，SD卡根目录下
     */
    public static final String APP_CONFIG_DIR = ROOT_PATH + File.separator + "config.json";
    /**
     * PATH_LOGZIP: 已经压缩过的zip,未上传过服务器的放在此文件夹下面 *
     */
    public static String LOG_UNUPLOAD_PATH = LOG_PATH + File.separator + "unUpload";
    /**
     * PATH_LOG_UPLOADED: 已经压缩过的zip,并且上传服务器成功后,移动到此文件夹下面 *
     */
    public static String LOG_UPLOADED_PATH = LOG_PATH + File.separator + "uploaded";
    /**
     * PATH_FIRMWARE: 该目录下存放固件更新相关的数据 *
     */
    public static String PATH_FIRMWARE = ROOT_PATH + File.separator + "update_firmware" + File.separator;
    /**
     * PATH_FIRMWARE_INFO: 服务器的所有固件info,这是一个文件,不是文件夹 *
     */
    public static String PATH_FIRMWARE_INFO = PATH_FIRMWARE
            + "firmware_info_json";
    /**
     * PATH_FIRMWARE_INFO: 服务器的所有固件info,这是一个文件,不是文件夹 *
     */
    public static String PATH_FIRMWARE_INFO_2 = PATH_FIRMWARE
            + "firmware_info_json_2";
    /**
     * STTC_PATH: 保存STTC的根目录 *
     */
    public static String STTC_PATH = ROOT_PATH + File.separator + "sttc" + File.separator;
    /**
     * STTC_PATH: 保存STTC的根目录 广播包 *
     */
    public static String STTC_PATH1 = ROOT_PATH + File.separator + "sttc1" + File.separator;
    /**
     * STTC_UPDATED_PATH: 已经上传完成的STTC文件目录 *
     */
    public static String STTC_UPDATED_PATH = ROOT_PATH + File.separator + "processed" + File.separator;
    /**
     * 相册的路径
     */
    public static String PICTURE_ROOT_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
            + File.separator;
    //-----------------------测试算法,模拟数据存放的位置----------------------------
    /**
     * 模拟数据存放位置的名字
     */
    private static final String TEST_DATA = "test_data";
    /**
     * 模拟数据存放位置的根路径
     */
    public static final String TEST_DATA_PATH = ROOT_PATH + File.separator + TEST_DATA;
}
