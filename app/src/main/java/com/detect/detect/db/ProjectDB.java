package com.detect.detect.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.detect.detect.shared_preferences.TestPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class ProjectDB {
    public synchronized boolean insertTestPoint(String dbName, SQLiteDatabase database, int buildSerialNum, int[] advArr,
                                                String constructionOrganization, String coordinateInfo,
                                                String detectPerson, long detectTime, String fillerType,
                                                String instrumentNumber, String picPath, String projectName) {
        if (TextUtils.isEmpty(dbName)) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProjectMetadata.BUILD_SERIAL_NUM, buildSerialNum);
        contentValues.put(ProjectMetadata.ADV_Arr, Arrays.toString(advArr));
        contentValues.put(ProjectMetadata.CONSTRUCTION_ORGANIZATION, constructionOrganization);
        contentValues.put(ProjectMetadata.COORDINATE_INFO, coordinateInfo);
        contentValues.put(ProjectMetadata.DETECT_PERSON, detectPerson);
        contentValues.put(ProjectMetadata.DETECT_TIME, detectTime);
        contentValues.put(ProjectMetadata.FILLER_TYPE, fillerType);
        contentValues.put(ProjectMetadata.INSTRUMENT_NUMBER, instrumentNumber);
        contentValues.put(ProjectMetadata.PIC_PATH, picPath);
        contentValues.put(ProjectMetadata.PROJECT_NAME, projectName);

        long re = database.insert(dbName, null, contentValues);
//        database.close();
        if (re == -1) {
            return false;
        }
        return true;
    }

    public synchronized List<TestPoint> queryAllTestPointData(String tableName, SQLiteDatabase database) {

        ArrayList<TestPoint> entityList = new ArrayList<>();
        String[] columns = new String[]{
                ProjectMetadata.BUILD_SERIAL_NUM,
                ProjectMetadata.COORDINATE_INFO,
                ProjectMetadata.DETECT_TIME,
                ProjectMetadata.PROJECT_NAME,
                ProjectMetadata.CONSTRUCTION_ORGANIZATION,
                ProjectMetadata.FILLER_TYPE,
                ProjectMetadata.INSTRUMENT_NUMBER,
                ProjectMetadata.DETECT_PERSON,
                ProjectMetadata.PIC_PATH,
                ProjectMetadata.ADV_Arr
        };
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = ProjectMetadata.BUILD_SERIAL_NUM + " ASC";
        Cursor cursor = null;
        try {
            cursor = database.query(tableName, columns, selection, selectionArgs,
                    groupBy, having, orderBy);
            while (cursor.moveToNext()) {
                int build_serial_num = cursor.getInt(cursor.getColumnIndex(ProjectMetadata.BUILD_SERIAL_NUM));
                String coordinate_info = cursor.getString(cursor.getColumnIndex(ProjectMetadata.COORDINATE_INFO));
                int detect_time = cursor.getInt(cursor.getColumnIndex(ProjectMetadata.DETECT_TIME));
                String project_name = cursor.getString(cursor.getColumnIndex(ProjectMetadata.PROJECT_NAME));
                String construction_organization = cursor.getString(cursor.getColumnIndex(ProjectMetadata.CONSTRUCTION_ORGANIZATION));
                String filler_type = cursor.getString(cursor.getColumnIndex(ProjectMetadata.FILLER_TYPE));
                String instrument_number = cursor.getString(cursor.getColumnIndex(ProjectMetadata.INSTRUMENT_NUMBER));
                String detect_person = cursor.getString(cursor.getColumnIndex(ProjectMetadata.DETECT_PERSON));
                String pic_path = cursor.getString(cursor.getColumnIndex(ProjectMetadata.PIC_PATH));
                String adv_arr = cursor.getString(cursor.getColumnIndex(ProjectMetadata.ADV_Arr));
                entityList.add(new TestPoint(build_serial_num, coordinate_info, detect_time, project_name, construction_organization, filler_type, instrument_number, detect_person, pic_path, adv_arr));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
//        database.close();
        return entityList;
    }


    public synchronized boolean deleteProject(String tableName, SQLiteDatabase database) {
        database.execSQL("DROP TABLE " + tableName);
        return true;
    }

    public synchronized boolean deleteTestPoint(String tableName, SQLiteDatabase database, int buildSerialNum) {
        if (buildSerialNum < 0) {
            return false;
        }
        String whereClause = ProjectMetadata.BUILD_SERIAL_NUM + " = ?";
        String[] whereArgs = new String[]{buildSerialNum + ""};
        int flag = database.delete(tableName, whereClause, whereArgs);
//        database.close();
        return flag >= 0;
    }

    public synchronized String getTestPointPicPath(String tableName, SQLiteDatabase database, int buildSerialNum) {
        if (buildSerialNum < 0) {
            return null;
        }
        String picPath = null;
        String[] columns = new String[]{
                ProjectMetadata.PIC_PATH
        };
        String selection = ProjectMetadata.BUILD_SERIAL_NUM + " = ? ";
        String[] selectionArgs = new String[]{buildSerialNum + ""};
        String groupBy = null;
        String having = null;
        String orderBy = ProjectMetadata.BUILD_SERIAL_NUM + " ASC";
        Cursor cursor = null;
        try {
            cursor = database.query(tableName, columns, selection, selectionArgs,
                    groupBy, having, orderBy);
            while (cursor.moveToNext()) {
                picPath = cursor.getString(cursor.getColumnIndex(ProjectMetadata.PIC_PATH));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            //database.close();
        }
        return picPath;
    }

    public synchronized int queryMaxBuildSerialNum(String tableName, SQLiteDatabase database) {
        int buildSerialNum = -1;
        Cursor cursor = null;
        try {
            String sql = "SELECT " + ProjectMetadata.BUILD_SERIAL_NUM +
                    " FROM " + tableName +
                    " ORDER BY " + ProjectMetadata.BUILD_SERIAL_NUM + " DESC LIMIT 1";
            cursor = database.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                return buildSerialNum;
            }
            while (cursor.moveToNext()) {
                buildSerialNum = cursor.getInt(cursor.getColumnIndex(ProjectMetadata.BUILD_SERIAL_NUM));
            }
            return buildSerialNum;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
//            database.close();
        }
    }

    private static final String TAG = "ProjectDB";

    public synchronized int queryTableItemNum(SQLiteDatabase database, String dbName) {
        int count = -1;
        Cursor cursor = null;
//        String sql = "select count(*) from info";
//        Cursor cursor = db.rawQuery(sql, null);
//        cursor.moveToFirst();
//        long count = cursor.getLong(0);
//        cursor.close();
        try {
            String sql = "select count(*) from " + dbName;
            cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
//            database.close();
        }
    }

    public List<String> getTableNameList(SQLiteDatabase database) {
//        SQLiteDatabase database = mDbContext.openOrCreateDatabase(DbConstant.TEMPERATURE_DB, Context.MODE_PRIVATE, null);
        ArrayList<String> tableNameList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = " SELECT tbl_name FROM sqlite_master WHERE type = 'table' ";

            cursor = database.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                return null;
            }
            while (cursor.moveToNext()) {
                String tableName = cursor.getString(0);
                if (!TextUtils.isEmpty(tableName)) {
                    tableNameList.add(tableName);
                }
            }
            return tableNameList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
//            database.close();
        }
    }
}