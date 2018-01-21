package com.detect.detect.shared_preferences;

import android.content.Context;
import android.text.TextUtils;

import com.detect.detect.db.DataDBManager;
import com.detect.detect.db.FileConstant;
import com.detect.detect.db.IDataManager;
import com.detect.detect.db.MultiDataDBManager;
import com.detect.detect.utils.FileUtils;
import com.detect.detect.utils.MD5Utils;
import com.gsh.utils.Base64Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class ProjectDataManagerDB implements IProjectDataManager {
    private static Context mContext;

    public static void initialize(Context context) {
        mContext = context;
        MultiDataDBManager.initialize(mContext);
    }

    private static ProjectDataManagerDB mProjectDataManagerDB = null;

    private ProjectDataManagerDB() {
    }

    public static ProjectDataManagerDB getInstance() {
        if (mContext == null) {
            throw new IllegalArgumentException("请先调用初始化方法initialize()");
        }
        if (mProjectDataManagerDB == null) {
            synchronized (ProjectDataManagerDB.class) {
                if (mProjectDataManagerDB == null) {
                    mProjectDataManagerDB = new ProjectDataManagerDB();
                }
            }
        }
        return mProjectDataManagerDB;
    }

    @Override
    public void insertTestPoint(String projectName, TestPoint testPoint) {
        IDataManager dataDBManager = MultiDataDBManager.getDataDBManager(projectName);
        dataDBManager.insertTestPoint(testPoint);
    }

    @Override
    public List<Project> getAllProjects() {
//        List<String> projectBase64List = FileUtils.getProjectMD5NameList(FileConstant.DB_PATH);
//        if (projectBase64List == null || projectBase64List.size() == 0) {
//            return null;
//        }

        ArrayList<Project> projects = new ArrayList<>();

        for (String projectBase64 : projectBase64List) {
            if (TextUtils.isEmpty(projectBase64)) {
                continue;
            }
            IDataManager dataDBManager = MultiDataDBManager.getDataDBManager(projectBase64);
            if (dataDBManager == null) {
                continue;
            }
            List<TestPoint> testPoints = dataDBManager.queryAllTestPointData();
            if (testPoints == null || testPoints.size() == 0) {
                continue;
            }
            Project project = new Project();
            TestPoint testPoint = testPoints.get(0);
            project.setProjectName(testPoint.getProjectName());
            int maxBuildSerialNum = dataDBManager.queryMaxBuildSerialNum();
            project.setMaxBuildSerialNum(maxBuildSerialNum);
            List<TestPoint> testPointList = project.getTestPointList();
            testPointList.addAll(testPoints);
            projects.add(project);
        }
        return projects;
    }

    @Override
    public List<String> getAllProjectNames() {
        ArrayList<String> projectNameList = new ArrayList<>();
        List<String> projectUUidList = FileUtils.getProjectMD5NameList(FileConstant.DB_PATH);
        if (projectUUidList == null || projectUUidList.size() == 0) {
            return null;
        }
        for (String projectUUID : projectUUidList) {
            String projectName = Base64Utils.decode(projectUUID);
            if (!TextUtils.isEmpty(projectName)) {
                projectNameList.add(projectName);
            }
        }
        return projectNameList;
    }

    @Override
    public boolean isProjectNameExit(String projectName) {
        List<String> projectUUidList = FileUtils.getProjectMD5NameList(FileConstant.DB_PATH);
        if (projectUUidList == null || projectUUidList.size() == 0) {
            return false;
        }
        for (String projectUUID : projectUUidList) {
            String projectNameT = Base64Utils.decode(projectUUID);
            if (!TextUtils.isEmpty(projectNameT) && projectNameT.equals(projectName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMaxBuildSerialNum(String projectName) {
        IDataManager dataDBManager = MultiDataDBManager.getDataDBManager(projectName);
        if (dataDBManager == null) {
            return  -1;
        }
        return dataDBManager.queryMaxBuildSerialNum();
    }

    @Override
    public String getTestPointPicPath(String projectName, int buildSerialNum) {
        IDataManager dataDBManager = MultiDataDBManager.getDataDBManager(projectName);
        return dataDBManager.getTestPointPicPath(buildSerialNum);
    }

    @Override
    public boolean deleteTestPoint(String projectName, int buildSerialNum) {
        IDataManager dataDBManager = MultiDataDBManager.getDataDBManager(projectName);
        return dataDBManager.deleteTestPoint(buildSerialNum);

    }

    @Override
    public boolean deleteProject(String projectName) {
        IDataManager dataDBManager = MultiDataDBManager.getDataDBManager(projectName);
        return dataDBManager.deleteProject();
    }

    @Override
    public int queryTableItemNum(String projectName) {
        IDataManager dataDBManager = MultiDataDBManager.getDataDBManager(projectName);
        return dataDBManager.queryTableItemNum();
    }

    @Override
    public List<String> getTableList() {
        IDataManager dataDBManager = MultiDataDBManager.getDataDBManager(projectName);
        return dataDBManager.getTableList();
    }
}
