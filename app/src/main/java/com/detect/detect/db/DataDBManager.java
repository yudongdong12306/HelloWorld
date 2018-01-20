package com.detect.detect.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.detect.detect.shared_preferences.TestPoint;

import java.util.List;

/**
 * @author jun.wang@raiing.com.
 * @version 1.1 2015/6/15
 */
public class DataDBManager implements IDataManager {
    private static final String TAG = "DataDBManager";
    private final ProjectDB mProjectDB;
    private ProjectDBHelper mProjectDBHelper;
    /**
     * 数据库的名称
     */
    private String dbName;

    /**
     * 新版用到的，是根据用户uuid文件夹生成的数据库
     *
     * @param context 全局的Context
     * @param DBName  数据库名字,为用户uuid
     */
    public DataDBManager(Context context, String DBName) {
        this.dbName = DBName;
        // 解决保存数据库到SD卡
        DatabaseContext dbContext = new DatabaseContext(context, dbName);
        mProjectDBHelper = new ProjectDBHelper(dbContext);
        mProjectDB = new ProjectDB();
    }

    @Override
    public boolean insertTestPoint(TestPoint testPoint) {
        if (testPoint == null) {
            return false;
        }
        SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        int buildSerialNum = testPoint.getBuildSerialNum();
        int[] advArr = testPoint.getAdvArr();
        String constructionOrganization = testPoint.getConstructionOrganization();
        String coordinateInfo = testPoint.getCoordinateInfo();
        String detectPerson = testPoint.getDetectPerson();
        long detectTime = testPoint.getDetectTime();
        String fillerType = testPoint.getFillerType();
        String instrumentNumber = testPoint.getInstrumentNumber();
        String picPath = testPoint.getPicPath();
        String projectName = testPoint.getProjectName();
        if (buildSerialNum < 1) {
            return false;
        }
        if (advArr == null) {
            return false;
        }
        if (constructionOrganization == null) {
            return false;
        }
        if (coordinateInfo == null) {
            return false;
        }
        if (detectPerson == null) {
            return false;
        }
        if (detectTime < 0) {
            return false;
        }
        if (fillerType == null) {
            return false;
        }
        if (instrumentNumber == null) {
            return false;
        }
        if (picPath == null) {
            return false;
        }
        return projectName != null && mProjectDB.insertTestPoint(database, buildSerialNum, advArr, constructionOrganization, coordinateInfo, detectPerson, detectTime, fillerType, instrumentNumber, picPath, projectName);
    }

    @Override
    public List<TestPoint> queryAllTestPointData() {
        SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.queryAllTestPointData(database);
    }

    @Override
    public boolean deleteProject() {
        SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.deleteProject(database);
    }

    @Override
    public boolean deleteTestPoint(int buildSerialNum) {
        SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.deleteTestPoint(database,buildSerialNum);
    }

    @Override
    public String getTestPointPicPath(int buildSerialNum) {
        SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.getTestPointPicPath(database,buildSerialNum);
    }

    @Override
    public int queryMaxBuildSerialNum() {
        SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.queryMaxBuildSerialNum(database);
    }
}
