//package com.detect.detect.db.entity;
//
//import com.raiing.pudding.constant.UserConstant;
//
///**
// * 布丁专有的用户信息,对应用户数据库中的raiing_pudding_user表
// *
// * @author jun.wang@raiing.com.
// * @version 1.1 2015/6/5
// */
//public class UserPuddingDBEntity {
//
//
//    /**
//     * 用户的ID
//     */
//    private int userID;
//    /**
//     * 用户生日,unix时间戳,单位s。小于1970年，为负值
//     */
//    private long birthday = UserConstant.BIRTHDAY_DEFAULT_VALUE;
//
//    /**
//     * 用户性别
//     */
//    private int sex = -1;
//
//    /**
//     * 按时接种免疫
//     */
//    private int vaccination = -1;
//
//    /**
//     * 既往病史
//     */
//    private int medicalHistory = -1;
//
//    public UserPuddingDBEntity(int userID, long birthday, int sex, int vaccination, int medicalHistory) {
//        this.userID = userID;
//        this.birthday = birthday;
//        this.sex = sex;
//        this.vaccination = vaccination;
//        this.medicalHistory = medicalHistory;
//    }
//
//    public UserPuddingDBEntity() {
//    }
//
//    public int getUserID() {
//        return userID;
//    }
//
//    public void setUserID(int userID) {
//        this.userID = userID;
//    }
//
//    public long getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(long birthday) {
//        this.birthday = birthday;
//    }
//
//    public int getSex() {
//        return sex;
//    }
//
//    public void setSex(int sex) {
//        this.sex = sex;
//    }
//
//    public int getVaccination() {
//        return vaccination;
//    }
//
//    public void setVaccination(int vaccination) {
//        this.vaccination = vaccination;
//    }
//
//    public int getMedicalHistory() {
//        return medicalHistory;
//    }
//
//    public void setMedicalHistory(int medicalHistory) {
//        this.medicalHistory = medicalHistory;
//    }
//
//    @Override
//    public String toString() {
//        return "UserPuddingEntity{" +
//                "userID=" + userID +
//                ", birthday=" + birthday +
//                ", sex=" + sex +
//                ", vaccination=" + vaccination +
//                ", medicalHistory=" + medicalHistory +
//                '}';
//    }
//}
