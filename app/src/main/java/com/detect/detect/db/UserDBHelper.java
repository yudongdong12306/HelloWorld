package com.detect.detect.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.detect.detect.log.DetectLog;


/**
 * @author jun.wang@raiing.com.
 * @version 1.1 2015/6/5
 */
public class UserDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "UserDBHelper";

    /**
     * 创建通用的用户表
     */
    private static final String CREATE_RU = "CREATE TABLE IF NOT EXISTS " + UserMetadata.RaiingUser.TABLE_NAME_RU + "(\n" +
            UserMetadata.RaiingUser.KEY_ID_INT + " INTEGER PRIMARY KEY ASC AUTOINCREMENT, \n" +
            UserMetadata.RaiingUser.KEY_UUID_STR + " TEXT UNIQUE, \n" +
            UserMetadata.RaiingUser.KEY_CREATE_UUID_STR + " TEXT, \n" +
            UserMetadata.RaiingUser.KEY_EMAIL_STR + " TEXT, \n" +
            UserMetadata.RaiingUser.KEY_MOBILE_STR + " TEXT, \n" +
            UserMetadata.RaiingUser.KEY_NICK_STR + " TEXT NOT NULL, \n" +
            UserMetadata.RaiingUser.KEY_USER_IMG_STR + " TEXT, \n" +
            UserMetadata.RaiingUser.KEY_SCORE_INT + " INTEGER, \n" +
            UserMetadata.RaiingUser.KEY_MONEY_INT + " INTEGER, \n" +
            UserMetadata.RaiingUser.KEY_EMAIL_ACTIVE_INT + " INTEGER,\n" +
            UserMetadata.RaiingUser.KEY_REMARK_STR + " TEXT\n" +
            ");\n";

    /**
     * 创建布丁专用的用户表
     */
    private static final String CREATE_RPU = "CREATE TABLE IF NOT EXISTS " + UserMetadata.RaiingPuddingUser.TABLE_NAME_RPU + " (\n" +
            UserMetadata.RaiingPuddingUser.KEY_USER_ID_INT + " INTEGER UNIQUE,\n" +
            UserMetadata.RaiingPuddingUser.KEY_BIRTHDAY_INT + " INTEGER, \n" +
            UserMetadata.RaiingPuddingUser.KEY_SEX_INT + " INTEGER, \n" +
            UserMetadata.RaiingPuddingUser.KEY_VACCINATION_INT + " INTEGER, \n" +
            UserMetadata.RaiingPuddingUser.KEY_MEDICAL_HISTORY_INT + " INTEGER\n" +
            ");\n";

    /**
     * 创建布丁的用户关联表
     */
    private static final String CREATE_RPUR = "CREATE TABLE IF NOT EXISTS " + UserMetadata.RaiingPuddingUserRelation.TABLE_NAME_RPUR + " (\n" +
            UserMetadata.RaiingPuddingUserRelation.KEY_USER_ID_INT + " INTEGER NOT NULL, \n" +
            UserMetadata.RaiingPuddingUserRelation.KEY_PARENT_ID_INT + " INTEGER NOT NULL, \n" +
            UserMetadata.RaiingPuddingUserRelation.KEY_DATA_TYPE_INT + " INTEGER NOT NULL, \n" +
            UserMetadata.RaiingPuddingUserRelation.KEY_PROMISSION_ID_INT + " INTEGER NOT NULL, \n" +
            UserMetadata.RaiingPuddingUserRelation.KEY_RELATION_INT + " INTEGER, \n" +
            UserMetadata.RaiingPuddingUserRelation.KEY_STATUS_INT + " INTEGER, \n" +
            "UNIQUE (" + UserMetadata.RaiingPuddingUserRelation.KEY_PARENT_ID_INT + ", " + UserMetadata.RaiingPuddingUserRelation.KEY_USER_ID_INT + "," + UserMetadata.RaiingPuddingUserRelation.KEY_DATA_TYPE_INT + ")\n" +
            ");\n";

    /**
     * 用户的数据库的名称
     */
    private static final String DATABASE_NAME = "AccountForPudding.db";
    /**
     * 数据库的版本
     */
    private static final int DATABASE_VERSION = 1;


    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DetectLog.e(TAG + "====>> 创建用户数据库");
        // 通用的raiing用户表
        db.execSQL(CREATE_RU);
        // pudding专用的用户表
        db.execSQL(CREATE_RPU);
        // pudding专用的关系表
        db.execSQL(CREATE_RPUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DetectLog.e(TAG + "===>> 升级用户数据库");
    }
}
