package com.detect.detect.shared_preferences;

import android.content.Context;
import android.text.TextUtils;

import com.detect.detect.db.DataDBManager;
import com.detect.detect.utils.ToastUtils;

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
    public void insertTestPoint(String tableName, TestPoint testPoint) {
        mDataDBManager.insertTestPoint(testPoint, tableName);
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
//            int itemNum = queryTableItemNum(ProjectNameUUIDSP.getInstance().getProjectNameFromMd5(tableName));
            int itemNum = queryTableItemNum(tableName);
            if (itemNum <= 0) {
                //删除
                deleteProject(tableName);
                continue;
            }
            List<TestPoint> testPoints = mDataDBManager.queryAllTestPointData(tableName);
            if (testPoints == null || testPoints.size() == 0) {
                continue;
            }
            Project project = new Project();
            TestPoint testPoint = testPoints.get(0);
            project.setProjectName(testPoint.getProjectName());
//            int maxBuildSerialNum = mDataDBManager.queryMaxBuildSerialNum(ProjectNameUUIDSP.getInstance().getProjectNameFromMd5(tableName));
//            project.setMaxBuildSerialNum(maxBuildSerialNum);
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
            List<ProjectInfo> allProjectInfos = ProjectNameUUIDSP.getInstance().getAllProjectInfos();
            if (allProjectInfos == null) {
                ToastUtils.showToast("程序出问题了,请检查,是不是卸载重装了!");
                return null;
            }
            for (ProjectInfo allProjectInfo : allProjectInfos) {
                if (allProjectInfo == null) {
                    continue;
                }
                String uuid = allProjectInfo.getUuid();
                if (TextUtils.equals(uuid, tableName)) {
                    projectNameList.add(allProjectInfo.getProjectName());
                }

            }
        }
        return projectNameList;
    }

    @Override
    public boolean isProjectNameExit(String tableName) {
        return mDataDBManager.isProjectNameExit(tableName);
    }

    @Override
    public int getMaxBuildSerialNum(String tableName) {
        return mDataDBManager.queryMaxBuildSerialNum(tableName);
    }

    @Override
    public String getTestPointPicPath(String tableName, String buildSerialNum) {
        return mDataDBManager.getTestPointPicPath(tableName, buildSerialNum);
    }

    @Override
    public boolean deleteTestPoint(String tableName, String buildSerialNum) {
        return mDataDBManager.deleteTestPoint(tableName, buildSerialNum);

    }

    @Override
    public boolean deleteProject(String tableName) {
        return mDataDBManager.deleteProject(tableName);
    }

    @Override
    public int queryTableItemNum(String tableName) {
        return mDataDBManager.queryTableItemNum(tableName);
    }

    @Override
    public List<String> getTableNameList() {
        return mDataDBManager.getTableNameList();
    }
}
