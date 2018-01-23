package com.detect.detect.shared_preferences;

import android.content.Context;
import android.text.TextUtils;

import com.detect.detect.db.DataDBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class ProjectDataManager implements IProjectDataManager {
    private static Context mContext;
    private DataDBManager mDataDBManager;

    public static void initialize(Context context) {
        mContext = context;
    }
    private static ProjectDataManager mProjectDataManager = null;

    private ProjectDataManager() {
        mDataDBManager = new DataDBManager(mContext);
    }

    public static ProjectDataManager getInstance() {
        if (mContext == null) {
            throw new IllegalArgumentException("请先调用初始化方法initialize()");
        }
        if (mProjectDataManager == null) {
            synchronized (ProjectDataManager.class) {
                if (mProjectDataManager == null) {
                    mProjectDataManager = new ProjectDataManager();
                }
            }
        }
        return mProjectDataManager;
    }

    @Override
    public void insertTestPoint(String projectName, TestPoint testPoint) {
        mDataDBManager.insertTestPoint(testPoint, projectName);
    }

    @Override
    public List<Project> getAllProjects() {
        List<String> tableNameList = getTableNameList();
        if (tableNameList == null || tableNameList.size() == 0) {
            return null;
        }
        ArrayList<Project> projects = new ArrayList<>();

        for (String tableName : tableNameList) {
            if (TextUtils.isEmpty(tableName) || !tableName.startsWith("project")) {
                continue;
            }
            int itemNum = queryTableItemNum(ProjectNameUUIDSP.getInstance().getProjectNameFromMd5(tableName));
            if (itemNum <= 0) {
                //删除
                deleteProject(ProjectNameUUIDSP.getInstance().getProjectNameFromMd5(tableName));
                continue;
            }
            List<TestPoint> testPoints = mDataDBManager.queryAllTestPointData(ProjectNameUUIDSP.getInstance().getProjectNameFromMd5(tableName));
            if (testPoints == null || testPoints.size() == 0) {
                continue;
            }
            Project project = new Project();
            TestPoint testPoint = testPoints.get(0);
            project.setProjectName(testPoint.getProjectName());
            int maxBuildSerialNum = mDataDBManager.queryMaxBuildSerialNum(ProjectNameUUIDSP.getInstance().getProjectNameFromMd5(tableName));
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
        List<String> tableNameList = getTableNameList();
        if (tableNameList == null || tableNameList.size() == 0) {
            return null;
        }
        for (String tableName : tableNameList) {
            if (TextUtils.isEmpty(tableName)) {
                continue;
            }
            String projectName = ProjectNameUUIDSP.getInstance().getProjectNameFromMd5(tableName);
            if (TextUtils.isEmpty(projectName)) {
                continue;
            }
            projectNameList.add(projectName);
        }
        return projectNameList;
    }

    @Override
    public boolean isProjectNameExit(String projectName) {
        return mDataDBManager.isProjectNameExit(projectName);
    }

    @Override
    public int getMaxBuildSerialNum(String projectName) {
        return mDataDBManager.queryMaxBuildSerialNum(projectName);
    }

    @Override
    public String getTestPointPicPath(String projectName, int buildSerialNum) {
        return mDataDBManager.getTestPointPicPath(projectName, buildSerialNum);
    }

    @Override
    public boolean deleteTestPoint(String projectName, int buildSerialNum) {
        return mDataDBManager.deleteTestPoint(projectName, buildSerialNum);

    }

    @Override
    public boolean deleteProject(String projectName) {
        return mDataDBManager.deleteProject(projectName);
    }

    @Override
    public int queryTableItemNum(String projectName) {
        return mDataDBManager.queryTableItemNum(projectName);
    }

    @Override
    public List<String> getTableNameList() {
        return mDataDBManager.getTableNameList();
    }
}
