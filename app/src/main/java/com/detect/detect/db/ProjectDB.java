package com.detect.detect.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.detect.detect.shared_preferences.TestPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class ProjectDB {
    public boolean insertTestPoint(SQLiteDatabase database, int buildSerialNum, int[] advArr,
                                   String constructionOrganization, String coordinateInfo,
                                   String detectPerson, long detectTime, String fillerType,
                                   String instrumentNumber, String picPath, String projectName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProjectMetadata.BUILD_SERIAL_NUM, buildSerialNum);
        contentValues.put(ProjectMetadata.KEY_ID_INT, Arrays.toString(advArr));
        contentValues.put(ProjectMetadata.CONSTRUCTION_ORGANIZATION, constructionOrganization);
        contentValues.put(ProjectMetadata.COORDINATE_INFO, coordinateInfo);
        contentValues.put(ProjectMetadata.DETECT_PERSON, detectPerson);
        contentValues.put(ProjectMetadata.DETECT_TIME, detectTime);
        contentValues.put(ProjectMetadata.FILLER_TYPE, fillerType);
        contentValues.put(ProjectMetadata.INSTRUMENT_NUMBER, instrumentNumber);
        contentValues.put(ProjectMetadata.PIC_PATH, picPath);
        contentValues.put(ProjectMetadata.PROJECT_NAME, projectName);

        long re = database.insert(ProjectMetadata.PROJECT_DATA, null, contentValues);
        if (re == -1) {
            return false;
        }
        return true;
    }

    public List<TestPoint> queryAllTestPointData(SQLiteDatabase database) {

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
                ProjectMetadata.PIC_PATH
        };
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = ProjectMetadata.BUILD_SERIAL_NUM + " ASC";
        Cursor cursor = null;
        try {
            cursor = database.query(ProjectMetadata.PROJECT_DATA, columns, selection, selectionArgs,
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
                entityList.add(new TestPoint(build_serial_num, coordinate_info, detect_time, project_name, construction_organization, filler_type, instrument_number, detect_person, pic_path));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return entityList;
    }


    public boolean deleteProject(SQLiteDatabase database) {
        database.execSQL("DROP TABLE " + ProjectMetadata.PROJECT_DATA);
        return true;
    }

    public boolean deleteTestPoint(SQLiteDatabase database, int buildSerialNum) {
        if (buildSerialNum < 0) {
            return false;
        }
        String whereClause = ProjectMetadata.BUILD_SERIAL_NUM + " = ?";
        String[] whereArgs = new String[]{buildSerialNum + ""};
        int flag = database.delete(ProjectMetadata.PROJECT_DATA, whereClause, whereArgs);
        return flag >= 0;
    }

    public String getTestPointPicPath(SQLiteDatabase database, int buildSerialNum) {
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
            cursor = database.query(ProjectMetadata.PROJECT_DATA, columns, selection, selectionArgs,
                    groupBy, having, orderBy);
            while (cursor.moveToNext()) {
                picPath = cursor.getString(cursor.getColumnIndex(ProjectMetadata.PIC_PATH));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return picPath;
    }

    public int queryMaxBuildSerialNum(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery("SELECT max(" + ProjectMetadata.BUILD_SERIAL_NUM + ") FROM " + ProjectMetadata.PROJECT_DATA, null);
        int maxSerialNum = 0;
        try {
            while (cursor.moveToNext()) {
                maxSerialNum = cursor.getInt(cursor.getColumnIndex(ProjectMetadata.BUILD_SERIAL_NUM));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return maxSerialNum;
    }
}