package com.detect.detect.shared_preferences;

import java.io.Serializable;

/**
 * Created by yu on 18/1/14.
 */

public class TestPoint implements Serializable {
    private int buildSerialNum;
    private String coordinateInfo;
    private long detectTime;
    private String projectName;
    private String constructionOrganization;
    private String fillerType;
    private String instrumentNumber;
    private String detectPerson;
    private String picPath;

    public boolean isLatest() {
        return isLatest;
    }

    public void setLatest(boolean latest) {
        isLatest = latest;
    }

    private boolean isLatest;

    public int getBuildSerialNum() {
        return buildSerialNum;
    }

    public void setBuildSerialNum(int buildSerialNum) {
        this.buildSerialNum = buildSerialNum;
    }

    public String getCoordinateInfo() {
        return coordinateInfo;
    }

    public void setCoordinateInfo(String coordinateInfo) {
        this.coordinateInfo = coordinateInfo;
    }

    public long getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(long detectTime) {
        this.detectTime = detectTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getConstructionOrganization() {
        return constructionOrganization;
    }

    public void setConstructionOrganization(String constructionOrganization) {
        this.constructionOrganization = constructionOrganization;
    }

    public String getFillerType() {
        return fillerType;
    }

    public void setFillerType(String fillerType) {
        this.fillerType = fillerType;
    }

    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    public void setInstrumentNumber(String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    public String getDetectPerson() {
        return detectPerson;
    }

    public void setDetectPerson(String detectPerson) {
        this.detectPerson = detectPerson;
    }

    @Override
    public String toString() {
        return "TestPoint{" +
                "buildSerialNum='" + buildSerialNum + '\'' +
                ", coordinateInfo='" + coordinateInfo + '\'' +
                ", detectTime=" + detectTime +
                ", projectName='" + projectName + '\'' +
                ", constructionOrganization='" + constructionOrganization + '\'' +
                ", fillerType='" + fillerType + '\'' +
                ", instrumentNumber='" + instrumentNumber + '\'' +
                ", detectPerson='" + detectPerson + '\'' +
                '}';
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
