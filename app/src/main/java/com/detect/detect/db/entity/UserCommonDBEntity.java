package com.detect.detect.db.entity;

/**
 * 通用的用户信息，对应数据库中raiing_user表
 *
 * @author jun.wang@raiing.com.
 * @version 1.1 2015/6/5
 */
public class UserCommonDBEntity {

    /**
     * 用户的ID,不可以为空
     */
    private int userID;
    /**
     * 用户的UUID
     */
    private String uuid;

    /**
     * 创建该用户的UUID
     */
    private String createUuid;
    /**
     * 用户的邮箱,可以为空
     */
    private String email;
    /**
     * 用户的手机,可以为空
     */
    private String mobile;
    /**
     * 用户的呢称，不能为空
     */
    private String nick;
    /**
     * 用户的头像URL,可以为空
     */
    private String userImgURL;
    /**
     * 积分，可以为空
     */
    private int score;
    /**
     * 虚拟货币,可以为空
     */
    private int money;
    /**
     * 邮箱是否激活，可以为空
     */
    private int emailActive;
    /**
     * 备注,可以为空
     */
    private String remark;

    /**
     * 通用字段构造用户的通用信息类
     *
     * @param userID      用户的ID
     * @param uuid        用户的UUID
     * @param createUuid  创建该用户的UUID
     * @param email       用户的email
     * @param mobile      用户的手机
     * @param nick        用户昵称
     * @param userImgURL  用户的头像URL
     * @param score       用户的积分
     * @param money       用户持用虚拟货币
     * @param emailActive 邮箱是否激活，1->激活,2->未激活
     * @param remark      备注
     */
    public UserCommonDBEntity(int userID, String uuid, String createUuid, String email, String mobile, String nick, String userImgURL, int score, int money, int emailActive, String remark) {
        this.userID = userID;
        this.uuid = uuid;
        this.createUuid = createUuid;
        this.email = email;
        this.mobile = mobile;
        this.nick = nick;
        this.userImgURL = userImgURL;
        this.score = score;
        this.money = money;
        this.emailActive = emailActive;
        this.remark = remark;
    }

    public UserCommonDBEntity() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public String getCreateUuid() {
        return createUuid;
    }

    public void setCreateUuid(String createUuid) {
        this.createUuid = createUuid;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUserImgURL() {
        return userImgURL;
    }

    public void setUserImgURL(String userImgURL) {
        this.userImgURL = userImgURL;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getEmailActive() {
        return emailActive;
    }

    public void setEmailActive(int emailActive) {
        this.emailActive = emailActive;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "UserCommonEntity{" +
                "uuid='" + uuid + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", nick='" + nick + '\'' +
                ", userImgURL='" + userImgURL + '\'' +
                ", score=" + score +
                ", money=" + money +
                ", emailActive=" + emailActive +
                ", remark='" + remark + '\'' +
                '}';
    }
}
