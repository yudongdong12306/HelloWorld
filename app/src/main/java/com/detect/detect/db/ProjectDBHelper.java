package com.detect.detect.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 温度数据库
 * Created by longfei.zhang on 2016/5/17.
 */
public class ProjectDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;


    /**
     * 温度数据表
     */
    private static final String CREATE_TEMPERATURE_TABLE = "CREATE TABLE IF NOT EXISTS " + ProjectMetadata.PROJECT_DATA + "( \n" +
            ProjectMetadata.KEY_ID_INT + " INTEGER PRIMARY KEY ASC ON CONFLICT REPLACE AUTOINCREMENT,\n" +
            ProjectMetadata.BUILD_SERIAL_NUM + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE,\n" +
            ProjectMetadata.CONSTRUCTION_ORGANIZATION + " INTEGER NOT NULL,\n" +
            ProjectMetadata.COORDINATE_INFO + " INTEGER NOT NULL,\n" +
            ProjectMetadata.DETECT_PERSON + " INTEGER NOT NULL,\n" +
            ProjectMetadata.DETECT_TIME + " INTEGER NOT NULL,\n" +
            ProjectMetadata.FILLER_TYPE + " INTEGER NOT NULL,\n" +
            ProjectMetadata.INSTRUMENT_NUMBER + " TEXT NOT NULL,\n" +
            ProjectMetadata.PROJECT_NAME + " TEXT NOT NULL,\n" +
            ProjectMetadata.PIC_PATH + " TEXT NOT NULL,\n" +
            ");";

    public ProjectDBHelper(Context context) {
        super(context, DbConstant.TEMPERATURE_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEMPERATURE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
