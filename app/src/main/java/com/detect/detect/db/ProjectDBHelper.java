package com.detect.detect.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

/**
 * 温度数据库
 * Created by longfei.zhang on 2016/5/17.
 */
public class ProjectDBHelper/* extends SQLiteOpenHelper*/ {
    //    /**
//     * 温度数据表
//     */
//    private static final String CREATE_TEMPERATURE_TABLE = "CREATE TABLE IF NOT EXISTS " + ProjectMetadata.PROJECT_DATA + " ( \n" +
//            ProjectMetadata.KEY_ID_INT + " INTEGER PRIMARY KEY ASC ON CONFLICT REPLACE AUTOINCREMENT,\n" +
//            ProjectMetadata.BUILD_SERIAL_NUM + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE,\n" +
//            ProjectMetadata.CONSTRUCTION_ORGANIZATION + " INTEGER NOT NULL,\n" +
//            ProjectMetadata.COORDINATE_INFO + " INTEGER NOT NULL,\n" +
//            ProjectMetadata.DETECT_PERSON + " INTEGER NOT NULL,\n" +
//            ProjectMetadata.DETECT_TIME + " INTEGER NOT NULL,\n" +
//            ProjectMetadata.FILLER_TYPE + " INTEGER NOT NULL,\n" +
//            ProjectMetadata.INSTRUMENT_NUMBER + " TEXT NOT NULL,\n" +
//            ProjectMetadata.TABLE_NAME + " TEXT NOT NULL,\n" +
//            ProjectMetadata.ADV_Arr + " TEXT NOT NULL,\n" +
//            ProjectMetadata.PIC_PATH + " TEXT NOT NULL ); ";
    private static final String TAG = "ProjectDBHelper";

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    private final SQLiteDatabase mDatabase;

    public ProjectDBHelper(Context context,String tableName) {
        mDatabase = context.openOrCreateDatabase(DbConstant.TEMPERATURE_DB, Context.MODE_PRIVATE, null);
//        super(context, DbConstant.TEMPERATURE_DB, null, DATABASE_VERSION);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("输入的数据库名称错误");
        }
        Log.d(TAG, "ProjectDBHelper() called with: context = [" + context + "], tableName = [" + tableName + "]");
        String create_temperature_table = "CREATE TABLE IF NOT EXISTS " + tableName + " ( \n" +
                ProjectMetadata.KEY_ID_INT + " INTEGER PRIMARY KEY ASC ON CONFLICT REPLACE AUTOINCREMENT,\n" +
                ProjectMetadata.BUILD_SERIAL_NUM + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE,\n" +
                ProjectMetadata.CONSTRUCTION_ORGANIZATION + " INTEGER NOT NULL,\n" +
                ProjectMetadata.COORDINATE_INFO + " INTEGER NOT NULL,\n" +
                ProjectMetadata.DETECT_PERSON + " INTEGER NOT NULL,\n" +
                ProjectMetadata.DETECT_TIME + " INTEGER NOT NULL,\n" +
                ProjectMetadata.FILLER_TYPE + " INTEGER NOT NULL,\n" +
                ProjectMetadata.INSTRUMENT_NUMBER + " TEXT NOT NULL,\n" +
                ProjectMetadata.PROJECT_NAME + " TEXT NOT NULL,\n" +
                ProjectMetadata.ADV_Arr + " TEXT NOT NULL,\n" +
                ProjectMetadata.PIC_PATH + " TEXT NOT NULL ); ";
        mDatabase.execSQL(create_temperature_table);
    }

}
