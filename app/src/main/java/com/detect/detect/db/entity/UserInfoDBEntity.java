//package com.detect.detect.db.entity;
//
///**
// * 用户的所有的信息，包含通用信息，pudding用户专有信息，还有关联信息
// *
// * @author jun.wang@raiing.com.
// * @version 1.1 2015/6/8
// */
//public class UserInfoDBEntity {
//    /**
//     * 通用的用户信息
//     */
//    private UserCommonDBEntity commonUserInfo;
//    /**
//     * 布丁专用的用户信息
//     */
//    private UserPuddingDBEntity puddingUserInfo;
//
//    /**
//     * 关联的用户的信息
//     */
//    private UserPuddingRelationDBEntity relationUserInfo;
//
//    public UserInfoDBEntity(UserCommonDBEntity commonUserInfo, UserPuddingDBEntity puddingUserInfo, UserPuddingRelationDBEntity relationUserInfo) {
//        this.commonUserInfo = commonUserInfo;
//        this.puddingUserInfo = puddingUserInfo;
//        this.relationUserInfo = relationUserInfo;
//    }
//
//    public UserInfoDBEntity() {
//        // 初始化
//        init();
//    }
//
//    private void init() {
//        commonUserInfo = new UserCommonDBEntity();
//        puddingUserInfo = new UserPuddingDBEntity();
//        relationUserInfo = new UserPuddingRelationDBEntity();
//    }
//
//    public UserCommonDBEntity getCommonUserInfo() {
//        return commonUserInfo;
//    }
//
//    public void setCommonUserInfo(UserCommonDBEntity commonUserInfo) {
//        this.commonUserInfo = commonUserInfo;
//    }
//
//    public UserPuddingDBEntity getPuddingUserInfo() {
//        return puddingUserInfo;
//    }
//
//    public void setPuddingUserInfo(UserPuddingDBEntity puddingUserInfo) {
//        this.puddingUserInfo = puddingUserInfo;
//    }
//
//    public UserPuddingRelationDBEntity getRelationUserInfo() {
//        return relationUserInfo;
//    }
//
//    public void setRelationUserInfo(UserPuddingRelationDBEntity relationUserInfo) {
//        this.relationUserInfo = relationUserInfo;
//    }
//
//    @Override
//    public String toString() {
//        return "UserInfoEntity{" +
//                "commonUserInfo=" + commonUserInfo +
//                ", puddingUserInfo=" + puddingUserInfo +
//                ", relationUserInfo=" + relationUserInfo +
//                '}';
//    }
//}
