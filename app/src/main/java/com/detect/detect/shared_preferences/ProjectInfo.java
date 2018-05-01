package com.detect.detect.shared_preferences;

import java.io.Serializable;

public class ProjectInfo implements Serializable {
    private String projectName;//
    private String constructionOrganization;//
    private String fillerType;//
    private String instrumentNumber;//
    private String detectPerson;//
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ProjectInfo() {
    }

    public ProjectInfo(String projectName, String constructionOrganization, String fillerType, String instrumentNumber, String detectPerson) {
        this.projectName = projectName;
        this.constructionOrganization = constructionOrganization;
        this.fillerType = fillerType;
        this.instrumentNumber = instrumentNumber;
        this.detectPerson = detectPerson;
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
        return "ProjectInfo{" +
                "projectName='" + projectName + '\'' +
                ", constructionOrganization='" + constructionOrganization + '\'' +
                ", fillerType='" + fillerType + '\'' +
                ", instrumentNumber='" + instrumentNumber + '\'' +
                ", detectPerson='" + detectPerson + '\'' +
                '}';
    }
}
