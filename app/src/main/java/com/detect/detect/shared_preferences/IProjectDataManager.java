package com.detect.detect.shared_preferences;

import java.util.List;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public interface IProjectDataManager {
    void insertTestPoint(String projectName, TestPoint testPoint);

    List<Project> getAllProjects();

    List<String> getAllProjectNames();

    boolean isProjectNameExit(String projectName);

    int getMaxBuildSerialNum(String projectName);

    String getTestPointPicPath(String projectName, String buildSerialNum);

    boolean deleteTestPoint(String projectName, String buildSerialNum);

    boolean deleteProject(String projectName);

    int queryTableItemNum(String projectName);

    List<String> getTableNameList();

    boolean updatePicPath(String tableName, String buildSerialNum, String path);
}
