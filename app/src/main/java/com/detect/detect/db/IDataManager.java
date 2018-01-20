package com.detect.detect.db;


import com.detect.detect.shared_preferences.TestPoint;

import java.util.List;

/**
 * @author jun.wang@raiing.com.
 * @version 1.1 2015/8/21
 */
public interface IDataManager {
    boolean insertTestPoint(TestPoint testPoint);

    List<TestPoint> queryAllTestPointData();

    boolean deleteProject();

    boolean deleteTestPoint(int buildSerialNum);

    String getTestPointPicPath(int buildSerialNum);

    int queryMaxBuildSerialNum();
}
