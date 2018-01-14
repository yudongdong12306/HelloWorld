package com.detect.detect.db.entity;

/**
 * 布丁用户间关系的实体
 *
 * @author jun.wang@raiing.com.
 * @version 1.1 2015/6/5
 */
public class UserPuddingRelationDBEntity {
    /**
     * 父用户的ID,不可以为空
     */
    private int parentID;
    /**
     * 关联用户的ID，不可以为空
     */
    private int userID;

    /**
     * 数据的类型，1 -> 发烧总监, 2 -> 孕律。不可以为空
     */
    private int dataType;

    /**
     * 允许对关联用户的数据的操作权限， 1 -> 所有, 2 -> 读取， 4 -> 写入,8 -> 删除。可以同时存在复合权限。不可以为空
     */
    private int permissionID;

    /**
     * 和关联用户之间的关系，总共有10中关系。详见UserConstant中的定义。可以为空
     */
    private int relation;

    /**
     * 关联用户的状态, 1->正常,2->停用,3->删除
     */
    private int status;

    public UserPuddingRelationDBEntity(int userID, int parentID, int dataType, int permissionID, int relation, int status) {
        this.userID = userID;
        this.parentID = parentID;
        this.dataType = dataType;
        this.permissionID = permissionID;
        this.relation = relation;
        this.status = status;
    }

    public UserPuddingRelationDBEntity() {
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserPuddingRelationEntity{" +
                "parentID=" + parentID +
                ", userID=" + userID +
                ", dataType=" + dataType +
                ", permissionID=" + permissionID +
                ", relation=" + relation +
                ", status=" + status +
                '}';
    }
}
