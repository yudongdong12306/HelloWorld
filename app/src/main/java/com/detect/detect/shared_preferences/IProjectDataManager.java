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

    String getTestPointPicPath(String projectName, int buildSerialNum);

    boolean deleteTestPoint(String projectName, int buildSerialNum);

    boolean deleteProject(String projectName);
}
