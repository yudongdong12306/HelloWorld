package com.detect.detect.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.TextUtils;


import java.io.File;
import java.io.IOException;


/**
 * 封装context类，保存数据库到SD卡
 */
public class DatabaseContext extends ContextWrapper {
    /**
     * 需要根据用户区分数据库
     */
    private String userUUID;

    /**
     * DB_PATH: 数据的保存的路径，目录 *
     */
//    public final static String DB_PATH = android.os.Environment
//            .getExternalStorageDirectory().getAbsolutePath()
//            + File.separator
//            + "pudding" + File.separator + "db" + File.separator;
    public DatabaseContext(Context base) {
        super(base);
    }

    public DatabaseContext(Context base, String userUUid) {
        super(base);
        this.userUUID = userUUid;
    }

    @Override
    public File getDatabasePath(String name) {
        // 判别是不是存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
        if (!sdExist) {// 若是不存在,
//            RaiingLog.e("SD卡办理：SD卡不存在，请加载SD卡");
            return null;
        } else {// 若是存在
            // 数据库途径
            String dbPath;

            // 判别目录是不是存在，不存在则创立该目录
            File dirFile;
            if (TextUtils.isEmpty(userUUID)) {
                dbPath = FileConstant.DB_PATH + name;
                dirFile = new File(FileConstant.DB_PATH);
            } else {
                dbPath = FileConstant.DB_PATH + userUUID + File.separator + name;
                dirFile = new File(FileConstant.DB_PATH + userUUID + File.separator);
            }
            if (!dirFile.exists())
                dirFile.mkdirs();

            // 数据库文件是不是创立成功
            boolean isFileCreateSuccess = false;
            // 判别文件是不是存在，不存在则创立该文件
            File dbFile = new File(dbPath);
            if (!dbFile.exists()) {
                try {
                    isFileCreateSuccess = dbFile.createNewFile();// 创立文件
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else
                isFileCreateSuccess = true;
            // 回来数据库文件目标
            if (isFileCreateSuccess) {
                return dbFile;
            } else {
                return null;
            }
        }
    }

    /**
     * 重载这个办法，是用来翻开SD卡上的数据库的，android 2.3及以下会调用这个办法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此办法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see ContextWrapper#openOrCreateDatabase(String,
     * int, CursorFactory,
     * DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               CursorFactory factory, DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                getDatabasePath(name), null);
        return result;
    }
}
