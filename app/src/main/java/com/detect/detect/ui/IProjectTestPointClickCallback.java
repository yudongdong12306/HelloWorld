package com.detect.detect.ui;

/**
 * Created by yu on 18/1/15.
 */

public interface IProjectTestPointClickCallback {
    void onTestPointLongClicked(String projectName, String buildSerialNum);
    void onTestPointClicked(String projectName, String buildSerialNum);
    void onProjectClicked(String projectName);
}
