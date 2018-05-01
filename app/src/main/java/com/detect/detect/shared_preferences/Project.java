package com.detect.detect.shared_preferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu on 18/1/14.
 */

public class Project implements Serializable {
//    private int maxBuildSerialNum;
    private String projectName;
    private List<TestPoint> testPointList = new ArrayList<>();


//    public int getMaxBuildSerialNum() {
//        return maxBuildSerialNum;
//    }
//
//    public void setMaxBuildSerialNum(int maxBuildSerialNum) {
//        this.maxBuildSerialNum = maxBuildSerialNum;
//    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public List<TestPoint> getTestPointList() {
        return testPointList;
    }

    @Override
    public String toString() {
        return "Project{" +
//                "maxBuildSerialNum='" + maxBuildSerialNum + '\'' +
                ", testPointList=" + testPointList +
                '}';
    }
}
