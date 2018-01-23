package com.detect.detect.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.detect.detect.shared_preferences.ProjectInfoSP;
import com.detect.detect.shared_preferences.ProjectNameUUIDSP;
import com.detect.detect.shared_preferences.TestPoint;

import java.util.List;

/**
 * @author jun.wang@raiing.com.
 * @version 1.1 2015/6/15
 */
public class DataDBManager implements IDataManager {
    private static final String TAG = "DataDBManager";
    //    private static DatabaseContext mDbContext;
//    private static DataDBManager mDataDBManager;
    private final ProjectDB mProjectDB;
    private final SQLiteDatabase mDatabase;
//    private String mTableName;
    //    private final ProjectDBHelper mProjectDBHelper;
    /**
     * 初始化
     *
     * @param context 全局的context，在Application中进行初始化
     */
//    public static void initialize(Context context) {
////        sContext = context;
//        mDbContext = new DatabaseContext(context);
//    }
//
//
//    public static DataDBManager getInstance() {
//        if ((mDbContext == null)) {
//            throw new IllegalArgumentException(TAG + "必须先调用initialize方法初始化为全局的Context对象");
//        }
//        if (null == mDataDBManager) {
//            synchronized (DataDBManager.class) {
//                if (null == mDataDBManager) {
//                    mDataDBManager = new DataDBManager();
//                }
//            }
//        }
//        return mDataDBManager;
//    }

    /**
     * 新版用到的，是根据用户uuid文件夹生成的数据库
     */
    public DataDBManager(Context context) {
        DatabaseContext mDbContext = new DatabaseContext(context);
        mDatabase = mDbContext.openOrCreateDatabase(DbConstant.TEMPERATURE_DB, Context.MODE_PRIVATE, null);
        mProjectDB = new ProjectDB();
    }

    @Override
    public boolean insertTestPoint(TestPoint testPoint, String projectName) {
        if (testPoint == null) {
            return false;
        }
        if (TextUtils.isEmpty(projectName)) {
            return false;
        }
        ProjectNameUUIDSP.getInstance().setProjectNameMd5(projectName);
        String tableName = ProjectNameUUIDSP.getInstance().getUUIDFromProjectName(projectName);
        createTableIfNeed(tableName);
        //SQLiteDatabase database = mProjectDBHelper.getDatabase();
        int buildSerialNum = testPoint.getBuildSerialNum();
        int[] advArr = testPoint.getAdvArr();
        String constructionOrganization = testPoint.getConstructionOrganization();
        String coordinateInfo = testPoint.getCoordinateInfo();
        String detectPerson = testPoint.getDetectPerson();
        long detectTime = testPoint.getDetectTime();
        String fillerType = testPoint.getFillerType();
        String instrumentNumber = testPoint.getInstrumentNumber();
        String picPath = testPoint.getPicPath();
        String projectNameT = testPoint.getProjectName();
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
        return projectName != null && mProjectDB.insertTestPoint(tableName, mDatabase, buildSerialNum, advArr, constructionOrganization, coordinateInfo, detectPerson, detectTime, fillerType, instrumentNumber, picPath, projectNameT);
    }

    private void createTableIfNeed(String tableName) {
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

    @Override
    public List<TestPoint> queryAllTestPointData(String projectName) {
        //SQLiteDatabase database = mProjectDBHelper.getDatabase();
        boolean isExit = isProjectNameExit(projectName);
        if (!isExit) {
            return null;
        }
        String tableName = ProjectNameUUIDSP.getInstance().getUUIDFromProjectName(projectName);
        return mProjectDB.queryAllTestPointData(tableName, mDatabase);
    }

    @Override
    public boolean deleteProject(String projectName) {
        boolean isExit = isProjectNameExit(projectName);
        if (!isExit) {
            return true;
        }
        String tableName = ProjectNameUUIDSP.getInstance().getUUIDFromProjectName(projectName);
        //SQLiteDatabase database = mProjectDBHelper.getDatabase();
        return mProjectDB.deleteProject(tableName, mDatabase);
    }

    @Override
    public boolean deleteTestPoint(String projectName, int buildSerialNum) {
        boolean isExit = isProjectNameExit(projectName);
        if (!isExit) {
            return true;
        }
        String tableName = ProjectNameUUIDSP.getInstance().getUUIDFromProjectName(projectName);
        //SQLiteDatabase database = mProjectDBHelper.getDatabase();
        return mProjectDB.deleteTestPoint(tableName, mDatabase, buildSerialNum);
    }

    @Override
    public String getTestPointPicPath(String projectName, int buildSerialNum) {
        boolean isExit = isProjectNameExit(projectName);
        if (!isExit) {
            return null;
        }
        String tableName = ProjectNameUUIDSP.getInstance().getUUIDFromProjectName(projectName);
        //SQLiteDatabase database = mProjectDBHelper.getDatabase();
        return mProjectDB.getTestPointPicPath(tableName, mDatabase, buildSerialNum);
    }

    @Override
    public int queryMaxBuildSerialNum(String projectName) {
        boolean isExit = isProjectNameExit(projectName);
        if (!isExit) {
            return -1;
        }
        String tableName = ProjectNameUUIDSP.getInstance().getUUIDFromProjectName(projectName);
        //SQLiteDatabase database = mProjectDBHelper.getDatabase();
        return mProjectDB.queryMaxBuildSerialNum(tableName, mDatabase);
    }

    @Override
    public int queryTableItemNum(String projectName) {
        boolean isExit = isProjectNameExit(projectName);
        if (!isExit) {
            return 0;
        }
        String tableName = ProjectNameUUIDSP.getInstance().getUUIDFromProjectName(projectName);
        //SQLiteDatabase database = mProjectDBHelper.getDatabase();
        return mProjectDB.queryTableItemNum(mDatabase, tableName);
    }

    @Override
    public List<String> getTableNameList() {
        return mProjectDB.getTableNameList(mDatabase);
    }

    @Override
    public boolean isProjectNameExit(String projectName) {
        List<String> tableNameList = getTableNameList();
        if (tableNameList == null || tableNameList.size() == 0) {
            return false;
        }
        for (String tableName : tableNameList) {
            if (TextUtils.isEmpty(tableName)) {
                continue;
            }
            String uuid = ProjectNameUUIDSP.getInstance().getUUIDFromProjectName(projectName);
            if (!TextUtils.isEmpty(tableName) && tableName.equals(uuid)) {
                return true;
            }

        }
        return false;
    }
}
