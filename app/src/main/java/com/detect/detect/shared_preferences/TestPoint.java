package com.detect.detect.shared_preferences;

import java.io.Serializable;

/**
 * Created by yu on 18/1/14.
 */

public class TestPoint implements Serializable {
    private String buildSerialNum;
    private String coordinateInfo;
    private int detectTime;
    private String projectName;
    private String constructionOrganization;
    private String fillerType;
    private String instrumentNumber;
    private String detectPerson;
    private String picPath;

    public String getWaveListStr() {
        return waveListStr;
    }

    public void setWaveListStr(String waveListStr) {
        this.waveListStr = waveListStr;
    }

    public String getSubsidence() {
        return subsidence;
    }

    public void setSubsidence(String subsidence) {
        this.subsidence = subsidence;
    }

    private String waveListStr;
    private String subsidence;
//    private int[] advArr = new int[1000];

    public String getProjectUUID() {
        return projectUUID;
    }

    public void setProjectUUID(String projectUUID) {
        this.projectUUID = projectUUID;
    }

    private String projectUUID;

    public TestPoint() {
    }

    public TestPoint(String build_serial_num, String coordinate_info, int detect_time,
                     String project_name, String construction_organization, String filler_type,
                     String instrument_number, String detect_person, String pic_path, String adv_arr) {
        this.buildSerialNum = build_serial_num;
        this.coordinateInfo = coordinate_info;
        this.detectTime = detect_time;
        this.projectName = project_name;
        this.constructionOrganization = construction_organization;
        this.fillerType = filler_type;
        this.instrumentNumber = instrument_number;
        this.detectPerson = detect_person;
        this.picPath = pic_path;
        this.waveListStr = adv_arr;
//        this.advArr=adv_arr.getBytes();
    }

//    public int[] getAdvArr() {
//        return advArr;
//    }


    public boolean isLatest() {
        return isLatest;
    }

    public void setLatest(boolean latest) {
        isLatest = latest;
    }

    private boolean isLatest;

    public String getBuildSerialNum() {
        return buildSerialNum;
    }

    public void setBuildSerialNum(String buildSerialNum) {
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

    public void setDetectTime(int detectTime) {
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
