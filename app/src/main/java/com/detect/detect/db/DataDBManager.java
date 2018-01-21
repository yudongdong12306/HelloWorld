package com.detect.detect.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.detect.detect.shared_preferences.TestPoint;
import com.gsh.utils.Base64Utils;

import java.util.List;

/**
 * @author jun.wang@raiing.com.
 * @version 1.1 2015/6/15
 */
public class DataDBManager implements IDataManager {
    private static final String TAG = "DataDBManager";
    private final ProjectDB mProjectDB;
    private final SQLiteDatabase mDB;
    private ProjectDBHelper mProjectDBHelper;
    private final String mDbName;
    //    private final String mDbName;

    /**
     * 新版用到的，是根据用户uuid文件夹生成的数据库
     *
     * @param context 全局的Context
     */
    public DataDBManager(Context context, String projectName) {
        // 解决保存数据库到SD卡
        DatabaseContext dbContext = new DatabaseContext(context);
        mDbName = Base64Utils.encode(projectName);
        ProjectDBHelper projectDBHelper = new ProjectDBHelper(dbContext);
        mDB = projectDBHelper.getDB();
        projectDBHelper.createTable(mDbName);
        mProjectDB = new ProjectDB();
    }

    @Override
    public boolean insertTestPoint(TestPoint testPoint) {
        if (testPoint == null) {
            return false;
        }
       // SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
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
        return projectName != null && mProjectDB.insertTestPoint(mDB, buildSerialNum, advArr, constructionOrganization, coordinateInfo, detectPerson, detectTime, fillerType, instrumentNumber, picPath, projectName);
    }

    @Override
    public List<TestPoint> queryAllTestPointData() {
       // SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.queryAllTestPointData(mDB);
    }

    @Override
    public boolean deleteProject() {
       // SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.deleteProject(mDB);
    }

    @Override
    public boolean deleteTestPoint(int buildSerialNum) {
       // SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.deleteTestPoint(mDB, buildSerialNum);
    }

    @Override
    public String getTestPointPicPath(int buildSerialNum) {
       // SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.getTestPointPicPath(mDB, buildSerialNum);
    }

    @Override
    public int queryMaxBuildSerialNum() {
       // SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.queryMaxBuildSerialNum(mDB);
    }

    @Override
    public int queryTableItemNum() {
       // SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.queryTableItemNum(mDB,mDbName);
    }

    @Override
    public List<String> getTableList() {
       // SQLiteDatabase database = mProjectDBHelper.getWritableDatabase();
        return mProjectDB.getTableNameList(mDB);
    }
}
