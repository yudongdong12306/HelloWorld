package com.detect.detect.ui;

/**
 * Created by yu on 18/1/15.
 */

public interface IProjectTestPointClickCallback {
    void onTestPointLongClicked(String projectName, int buildSerialNum);
    void onTestPointClicked(String projectName, int buildSerialNum);
    void onProjectClicked(String projectName);
}
