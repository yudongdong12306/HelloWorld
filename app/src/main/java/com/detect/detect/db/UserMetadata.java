package com.detect.detect.db;

/**
 * 用户数据库表结构的元数据
 * Created by jun on 15/8/11.
 */
public final class UserMetadata {

    private UserMetadata() {
    }

    /**
     * raiing通用用户的表结构
     */
    public abstract static class RaiingUser {
        /**
         * 通用的用户表,RU-> raiing_user
         */
        public static final String TABLE_NAME_RU = "raiing_user";

        /**
         * *************raiing user字段的key*****************
         */
        /**
         * raiing user表中的 id 字段
         */
        public static final String KEY_ID_INT = "id";
        /**
         * raiing user表中的 uuid 字段
         */
        public static final String KEY_UUID_STR = "uuid";
        /**
         * raiing user表中的 create_uuid 字段
         */
        public static final String KEY_CREATE_UUID_STR = "create_uuid";
        /**
         * raiing user表中的 email 字段
         */
        public static final String KEY_EMAIL_STR = "email";
        /**
         * raiing user表中的 mobile 字段
         */
        public static final String KEY_MOBILE_STR = "mobile";
        /**
         * raiing user表中的 nick 字段
         */
        public static final String KEY_NICK_STR = "nick";
        /**
         * raiing user表中的 user_img 字段
         */
        public static final String KEY_USER_IMG_STR = "user_img";
        /**
         * raiing user表中的 score 字段
         */
        public static final String KEY_SCORE_INT = "score";
        /**
         * raiing user表中的 money 字段
         */
        public static final String KEY_MONEY_INT = "money";
        /**
         * raiing user表中的 email_active 字段
         */
        public static final String KEY_EMAIL_ACTIVE_INT = "email_active";
        /**
         * raiing user表中的 remark 字段
         */
        public static final String KEY_REMARK_STR = "remark";
    }


    /**
     * 布丁用户信息表
     */
    public static abstract class RaiingPuddingUser {
        /**
         * 布丁用户信息表, RPU->raiing_pudding_user
         */
        public static final String TABLE_NAME_RPU = "raiing_pudding_user";


        /**
         * *************raiing pudding user字段的key*****************
         */
        /**
         * raiing pudding user表中的 user_id 字段
         */
        public static final String KEY_USER_ID_INT = "user_id";
        /**
         * raiing pudding user表中的 birthday 字段
         */
        public static final String KEY_BIRTHDAY_INT = "birthday";
        /**
         * raiing pudding user表中的 sex 字段
         */
        public static final String KEY_SEX_INT = "sex";
        /**
         * raiing pudding user表中的 vaccination 字段
         */
        public static final String KEY_VACCINATION_INT = "vaccination";
        /**
         * raiing pudding user表中的 medical_history 字段
         */
        public static final String KEY_MEDICAL_HISTORY_INT = "medical_history";
    }


    /**
     * 布丁用户关系表
     */
    public static abstract class RaiingPuddingUserRelation {
        /**
         * 布丁用户信息表, RPU->raiing_pudding_user
         */
        public static final String TABLE_NAME_RPUR = "raiing_pudding_user_relation";
        /**
         * *************raiing pudding user relation字段的key*****************
         */
        /**
         * raiing pudding user relation表中的 user_id 字段
         */
        public static final String KEY_USER_ID_INT = "user_id";
        /**
         * raiing pudding user relation表中的 parent_id 字段
         */
        public static final String KEY_PARENT_ID_INT = "parent_id";
        /**
         * raiing pudding user relation表中的 data_type 字段
         */
        public static final String KEY_DATA_TYPE_INT = "data_type";
        /**
         * raiing pudding user relation表中的 promission_id 字段
         */
        public static final String KEY_PROMISSION_ID_INT = "promission_id";
        /**
         * raiing pudding user relation表中的 relation 字段
         */
        public static final String KEY_RELATION_INT = "relation";
        /**
         * raiing pudding user relation表中的 status 字段
         */
        public static final String KEY_STATUS_INT = "status";
    }
}
