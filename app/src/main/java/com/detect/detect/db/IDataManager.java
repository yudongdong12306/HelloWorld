package com.detect.detect.db;


import com.detect.detect.shared_preferences.TestPoint;

import java.util.List;

/**
 * @author jun.wang@raiing.com.
 * @version 1.1 2015/8/21
 */
public interface IDataManager {
    boolean insertTestPoint(TestPoint testPoint, String tableName);

    List<TestPoint> queryAllTestPointData(String tableName);

    boolean deleteProject(String tableName);

    boolean deleteTestPoint(String tableName, String buildSerialNum);

    String getTestPointPicPath(String tableName, String buildSerialNum);

    int queryMaxBuildSerialNum(String tableName);

    int queryTableItemNum(String tableName);

    //    List<String> getTableList();
    List<String> getTableNameList();
    boolean isProjectNameExit(String projectName);
}
