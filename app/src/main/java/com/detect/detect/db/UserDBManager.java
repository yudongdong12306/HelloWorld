//package com.detect.detect.db;
//
//import android.app.Application;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.detect.detect.db.entity.UserCommonDBEntity;
//import com.detect.detect.db.entity.UserInfoDBEntity;
//import com.detect.detect.db.entity.UserPuddingDBEntity;
//import com.detect.detect.db.entity.UserPuddingRelationDBEntity;
//import com.detect.detect.log.DetectLog;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 用户数据库的操作。对应与用户数据表的增删改查操作
// *
// * @author jun.wang@raiing.com.
// * @version 1.1 2015/6/5
// */
//public class UserDBManager implements IUserManager {
//
//    private static final String TAG = "UserDBManager";
//
//    /**
//     * 用户数据库操作单例
//     */
//    private static UserDBManager instance = null;
//    private static Context mContext;
//    private UserDBHelper mHelper;
//
//    private UserDBManager(Context context) {
//        // 解决保存数据库到SD卡
//        DatabaseContext dbContext = new DatabaseContext(context);
////        UserDBHelper mHelper = new UserDBHelper(dbContext);
//        mHelper = new UserDBHelper(dbContext);
//        // 方法会抛出SQLException运行时异常
////        db = helper.getWritableDatabase();
//    }
//
//    /**
//     * 初始化
//     *
//     * @param context 全局的context，在application中进行初始化
//     */
//    public static void initialize(Context context) {
//        mContext = context;
//    }
//
//    /**
//     * 单例模式
//     *
//     * @return 用户数据库的操作实例
//     */
//    public static UserDBManager getInstance() {
//        if ((mContext == null) || !(mContext instanceof Application)) {
//            throw new IllegalArgumentException("必须先调用initialize方法初始化为全局的Context对象");
//        }
//        if (instance == null) {
//            synchronized (UserDBManager.class) {
//                if (instance == null) {
//                    instance = new UserDBManager(mContext);
//                }
//            }
//        }
//        return instance;
//    }
//
//
//    /**
//     * 创建可以登录的用户
//     *
//     * @param uuid        用户的UUID
//     * @param email       用户的email
//     * @param imageURL    用户头像的URL,没有可以设置为null或者""
//     * @param emailActive 用户邮箱的状态,1->激活,2->未激活
//     * @return 创建成功，返回true。否者返回false
//     */
//    @Override
//    public synchronized boolean createLoginUser(String uuid, String email, String mobile, String nickName, String imageURL, int emailActive) {
//        if (TextUtils.isEmpty(uuid) /*|| TextUtils.isEmpty(imageURL)*/) {
//            DetectLog.d("传入的参数为空,uuid为空");
//            return false;
//        }
//        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(mobile)) {
//            DetectLog.d("传入的参数，用户的邮箱和手机号不能同时为空");
//            return false;
//        }
//        String nickNameTemp = nickName;
//        if (TextUtils.isEmpty(nickNameTemp)) {
//            DetectLog.e("之前邮箱注册不需要输入用户昵称，现在无论是手机注册还是邮箱注册都需要输入用户昵称，如果为空，先做默认处理。其实此处应该抛出异常");
//            // 如果没有用户昵称，存入默认的昵称"default"
//            nickNameTemp = Constant.DEFAULT_NICKNAME;
//        }
//        // 自己创建自己,nickName就是邮箱名
//        // 打开数据库
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        // 开启事务
//        db.beginTransaction();
//        try {
//            boolean isSuccess = createUser(db, uuid, uuid, email, mobile, nickNameTemp, imageURL, -1, -1, UserConstant.BIRTHDAY_DEFAULT_VALUE, -1, -1, emailActive);
//            if (!isSuccess) {
//                return false;
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            // 结束事务
//            db.endTransaction();
//            // 关闭数据库
//            db.close();
//        }
//        return true;
//    }
//
//    /**
//     * 创建匿名用户，这些用户目前是不可以直接登录的
//     *
//     * @param uuid           用户的UUID
//     * @param createUUID     创建用户的UUID
//     * @param nickName       用户的名称
//     * @param imageURL       用户头像的URL,没有可以设置为null或者""
//     * @param relation       和关联用户的关系,有10种关系，详见UserConstant
//     * @param sex            用户的性别, 1-男,2-女,3-保密
//     * @param birthday       用户的生日,unix时间戳,单位s
//     * @param vaccination    疫苗接种情况,1-按时,2-未按时
//     * @param medicalHistory 是否有既往病史,1-是, 2-否
//     * @return 创建成功，返回true。否者返回false
//     */
//    @Override
//    public synchronized boolean createAnonymousUser(final String uuid,
//                                                    final String createUUID,
//                                                    final String nickName,
//                                                    final String imageURL,
//                                                    final int relation,
//                                                    final int sex,
//                                                    final long birthday,
//                                                    final int vaccination,
//                                                    final int medicalHistory) {
//        if (TextUtils.isEmpty(uuid)) {
//            DetectLog.d("传入的参数uuid为空");
//            return false;
//        }
//        if (TextUtils.isEmpty(createUUID)) {
//            DetectLog.d("传入的参数createUUID为空");
//            return false;
//        }
//        if (TextUtils.isEmpty(nickName)) {
//            DetectLog.d("传入的参数nickName为空");
//            return false;
//        }
//        // 打开数据库
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        // 开启事务
//        db.beginTransaction();
//        try {
//            boolean isSuccess = createUser(db, uuid, createUUID, null, null, nickName, imageURL, relation, sex, birthday, vaccination, medicalHistory, -1);
//            if (!isSuccess) {
//                return false;
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            // 结束事务
//            db.endTransaction();
//            // 关闭数据库
//            db.close();
//        }
//        return true;
//    }
//
//    @Override
//    public boolean saveAllUserFromServer(List<UserInfoEntity> entitys) {
//        return false;
//    }
//
//    @Override
//    public boolean saveAllUserFromServer(List<UserInfoEntity> entitys) {
//        return false;
//    }
//
//    @Override
//    public boolean saveAllUserFromServer(List<UserInfoEntity> entitys) {
//        return false;
//    }
//
//    @Override
//    public synchronized boolean saveAllUserFromServer(List<UserInfoEntity> entities) {
//        if (entities == null) {
//            DetectLog.e("从服务端下载的用户集合为null");
//            return false;
//        }
//        // 打开数据库
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        // 开启事务
//        db.beginTransaction();
//        try {
//            // 删除所有的用户数据
//            for (int i = 0; i < entities.size(); i++) {
////                String email = entities.get(i).getEmail();
////                if (TextUtils.isEmpty(email)) {
////                    continue;
////                } else {
////                    DetectLog.e("登录的邮箱为: " + email);
////                }
//                UserInfoEntity entity = entities.get(i);
//                // 用户的UUID
//                String uuid = entity.getUuid();
//                // 创建该用户的UUID
//                String createUUID = entity.getCreateUUID();
//                if (TextUtils.isEmpty(uuid)) {
//                    DetectLog.d("用户的UUID为空");
//                    return false;
//                }
//                if (TextUtils.isEmpty(createUUID)) {
//                    DetectLog.d("创建该用户的UUID为空");
//                    return false;
//                }
//                // 如果uuid不相同，说明不是登录用户
//                if (!uuid.equals(createUUID)) {
//                    continue;
//                }
////                int id = queryUserIDByEmail(db, email);
//                int id = queryUserIDByUUID(db, uuid);
//                // 不等于 -1,表示存在该用户
//                if (id != -1) {
//                    // 如果存在该用户，采取执行删除操作
////                    boolean re = deleteLoginUser(db, email);
//                    boolean re = deleteLoginUser(db, uuid);
//                    if (!re) {
//                        DetectLog.e("删除用户失败");
//                        return false;
//                    }
//                }
//
//            }
//            DetectLog.e("从服务端下载的用户数据集合为: " + entities);
//            // 重新插入所有的用户数据
//            for (int i = 0; i < entities.size(); i++) {
//                UserInfoEntity entity = entities.get(i);
//                if (entity.getStatus() != Constant.NORMAL_STATUS) {
//                    continue;
//                }
//                String email = entity.getEmail();
//                String mobile = entity.getMobile();
//                String uuid = entity.getUuid();
//                String createUUID = entity.getCreateUUID();
//                String nickName = entity.getNickName();
//                String photoPath = entity.getPhotoPath();
//                int relation = entity.getRelation();
//                int sex = entity.getSex();
//                long birthday = entity.getBirthday();
//                int vaccine = entity.getVaccine();
//                int condition = entity.getCondition();
//                String nick = nickName;
//
//                if (TextUtils.isEmpty(nick) && TextUtils.isEmpty(email) && TextUtils.isEmpty(mobile)) {
//                    //匿名用户不应该为空，错误的数据过滤掉
//                    DetectLog.e("出现匿名用户昵称为空的情况");
//                    continue;
//                } else if (TextUtils.isEmpty(nick) && (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(mobile))) {
//                    nick = Constant.DEFAULT_NICKNAME;
//                }
//                // 如果两者都为空，说明是匿名用户
//                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(mobile)) {
//                    // 匿名用户
//                    boolean re = createUser(db, uuid, createUUID, null, null, nick, photoPath, relation, sex, birthday, vaccine, condition, -1);
//                    if (!re) {
//                        DetectLog.e("插入匿名用户失败");
//                        return false;
//                    }
//                } else {
//                    // 可登陆用户
//                    boolean re = createUser(db, uuid, createUUID, email, mobile, nick, photoPath, relation, sex, birthday, vaccine, condition, entity.getActiveEmail());
//                    if (!re) {
//                        DetectLog.e("插入登录用户失败");
//                        return false;
//                    }
//                }
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            // 结束事务
//            db.endTransaction();
//            // 关闭数据库
//            db.close();
//        }
//
//        return true;
//    }
//
//
//    /**
//     * 创建用户
//     *
//     * @param db             数据库
//     * @param uuid           用户的UUID
//     * @param createUUID     创建用户的UUID
//     * @param email          用户邮箱
//     * @param mobile         用户的手机号
//     * @param nickName       用户的名称
//     * @param imageURL       用户头像的URL
//     * @param relation       和关联用户的关系
//     * @param sex            用户的性别, 1-男,2-女,3-保密
//     * @param birthday       用户的生日,单位s
//     * @param vaccination    疫苗接种情况,1-按时,2-未按时
//     * @param medicalHistory 是否有既往病史,1-是, 2-否
//     * @param emailActive    用户邮箱的状态,1->激活,2->未激活
//     * @return 创建成功，返回true。否者返回false
//     */
//    private boolean createUser(SQLiteDatabase db, String uuid, String createUUID, String email, String mobile, String nickName, String imageURL, int relation, int sex, long birthday, int vaccination, int medicalHistory, int emailActive) {
//
//        // 存储通用的用户信息
//        boolean isSuccess = saveRaiingCommonUser(db, uuid, createUUID, email, mobile, nickName, imageURL, emailActive);
//        if (!isSuccess) {
//            return false;
//        }
//        // 创建该用户的ID
//        int parentID = queryUserIDByUUID(db, createUUID);
//        if (parentID < 0) {
//            DetectLog.e(TAG + "====>父用户的ID为空");
//            return false;
//        }
//        // 用户的ID
//        int userID = queryUserIDByUUID(db, uuid);
//        if (userID < 0) {
//            DetectLog.e(TAG + "====>用户的ID为空");
//            return false;
//        }
//        // 保存pudding用户
//        isSuccess = savePuddingUser(db, userID, birthday, sex, vaccination, medicalHistory);
//        if (!isSuccess) {
//            return false;
//        }
//        // 保存用户间的关联关系
//        isSuccess = savePuddingUserRelation(db, parentID, userID, relation);
//        return isSuccess;
//    }
//
//
//    /**
//     * 保存通用的用户信息
//     *
//     * @param db          数据库
//     * @param uuid        用户的UUID
//     * @param createUUID  创建用户的UUID
//     * @param email       用户邮箱
//     * @param nickName    用户的昵称
//     * @param imageURL    用户头像的URL
//     * @param emailActive 邮箱是否激活
//     * @return 保存成功返回true
//     */
//    private boolean saveRaiingCommonUser(SQLiteDatabase db,
//                                         String uuid,
//                                         String createUUID,
//                                         String email,
//                                         String mobile,
//                                         String nickName,
//                                         String imageURL,
//                                         int emailActive) {
//        return saveRaiingCommonUser(db, uuid, createUUID, email, mobile, nickName, imageURL, -1, -1, emailActive, null);
//    }
//
//
//    /**
//     * 保存通用的用户信息，包含数据库中的全部字段
//     *
//     * @param db          数据库
//     * @param uuid        用户的uuid
//     * @param createUUID  创建用户的UUID
//     * @param email       用户邮箱，可以为空.为空时可传入null或""
//     * @param mobile      用户的手机号,可以为空,为空时可传入null或""
//     * @param nickName    用户昵称
//     * @param userImgURL  用户的头像URL,可以为空,为空时可传入null或""
//     * @param score       用户积分，可以为空,为空时可传入-1
//     * @param money       虚拟货币,可以为空,为空时可传入-1
//     * @param emailActive 邮箱是否激活,可以为空,为空时可传入-1
//     * @param remark      备注,可以为空,为空时可传入null或""
//     * @return 保存成功返回true
//     */
//    private boolean saveRaiingCommonUser(SQLiteDatabase db,
//                                         String uuid,
//                                         String createUUID,
//                                         String email,
//                                         String mobile,
//                                         String nickName,
//                                         String userImgURL,
//                                         int score,
//                                         int money,
//                                         int emailActive,
//                                         String remark) {
//        if (TextUtils.isEmpty(nickName)) {
//            DetectLog.e("用户的昵称不能为空");
//            return false;
//        }
//        ContentValues values = new ContentValues();
//        // 字段1
//        if (!TextUtils.isEmpty(uuid)) {
//            values.put(UserMetadata.RaiingUser.KEY_UUID_STR, uuid);
//        }
//        // 字段2
//        if (!TextUtils.isEmpty(createUUID)) {
//            values.put(UserMetadata.RaiingUser.KEY_CREATE_UUID_STR, createUUID);
//        }
//        // 字段3
//        if (!TextUtils.isEmpty(email)) {
//            values.put(UserMetadata.RaiingUser.KEY_EMAIL_STR, email);
//        }
//        // 字段4
//        if (!TextUtils.isEmpty(mobile)) {
//            values.put(UserMetadata.RaiingUser.KEY_MOBILE_STR, mobile);
//        }
//        // 字段5
//
//        values.put(UserMetadata.RaiingUser.KEY_NICK_STR, nickName);
//
//
//        // 字段6
//        if (!TextUtils.isEmpty(userImgURL)) {
//            values.put(UserMetadata.RaiingUser.KEY_USER_IMG_STR, userImgURL);
//        }
//        // 字段7
//        if (score >= 0) {
//            values.put(UserMetadata.RaiingUser.KEY_SCORE_INT, score);
//        }
//        // 字段8
//        if (money >= 0) {
//            values.put(UserMetadata.RaiingUser.KEY_MONEY_INT, money);
//        }
//        // 字段9
//        if (emailActive >= 0) {
//            values.put(UserMetadata.RaiingUser.KEY_EMAIL_ACTIVE_INT, emailActive);
//        }
//        // 字段10
//        if (!TextUtils.isEmpty(remark)) {
//            values.put(UserMetadata.RaiingUser.KEY_REMARK_STR, remark);
//        }
//        Log.e(TAG, "saveRaiingCommonUser: 需要保存的数据: " + values);
//        int size = values.size();
//        if (size > 0) {
//            // 通用的用户信息
//            long commonUserResult = db.insert(UserMetadata.RaiingUser.TABLE_NAME_RU, null, values);
//            if (commonUserResult == -1) {
//                DetectLog.e(TAG + "saveRaiingCommonUser: 创建common用户失败");
//                return false;
//            }
//        } else {
//            DetectLog.e(TAG + "saveRaiingCommonUser:　没有需要保存的通用的用户信息");
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * 保存布丁的用户信息
//     *
//     * @param db             数据库
//     * @param userID         对应通用用户表中的ID
//     * @param birthday       生日，UNIX时间戳，单位s
//     * @param sex            用户的性别, 1-男,2-女,3-保密。可以为空。为空时可以传入-1
//     * @param vaccination    疫苗接种情况,1-按时,2-未按时,可以为空,为空可以闯入-1
//     * @param medicalHistory 是否有既往病史,1-是, 2-否,可以为空,为空可以闯入-1
//     * @return 保存成功返回true
//     */
//    private boolean savePuddingUser(SQLiteDatabase db, int userID, long birthday, int sex, int vaccination, int medicalHistory) {
//        ContentValues values = new ContentValues();
//        values.put(UserMetadata.RaiingPuddingUser.KEY_USER_ID_INT, userID);
//        //不能判断>0，没有值服务器返回-1，不保存数据库的话，数据库默认是0
//        values.put(UserMetadata.RaiingPuddingUser.KEY_BIRTHDAY_INT, birthday);
//        values.put(UserMetadata.RaiingPuddingUser.KEY_SEX_INT, sex);
//        values.put(UserMetadata.RaiingPuddingUser.KEY_VACCINATION_INT, vaccination);
//        values.put(UserMetadata.RaiingPuddingUser.KEY_MEDICAL_HISTORY_INT, medicalHistory);
//
//        // pudding用户信息
//        long puddingUserResult = db.insert(UserMetadata.RaiingPuddingUser.TABLE_NAME_RPU, null, values);
//        if (puddingUserResult == -1) {
//            DetectLog.e(TAG + "======>创建pudding用户失败");
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 保存用户的间的关系
//     *
//     * @param db       数据库
//     * @param relation 关系，详见UserConstant
//     * @return 保存成功返回false
//     */
//    private boolean savePuddingUserRelation(SQLiteDatabase db, int parentID, int userID, int relation) {
//        // promissionID的权限为1->所有, dataType的数据类型为1-V2, status统一为1->正常
//        return savePuddingUserRelation(db, parentID, userID, 1, 1, relation, 1);
//    }
//
//
//    /**
//     * 保存用户直接的关系
//     *
//     * @param db           数据库
//     * @param parentID     父用户ID
//     * @param userID       用户ID
//     * @param promissionID 对应数据的操作权限,1->所有，2->读取,4->写入,8->删除,6->读取和写入
//     * @param dataType     数据的类型，1->V2、2->V3
//     * @param relation     用户之间的关系，总共十种关系，详见UserConstant。此字段可以为空,为空时传入-1
//     * @param status       用户的状态，1->正常,2->停用,3->删除
//     * @return 保存成功返回true
//     */
//    private boolean savePuddingUserRelation(SQLiteDatabase db, int parentID, int userID, int promissionID, int dataType, int relation, int status) {
//        ContentValues values = new ContentValues();
//        values.put(UserMetadata.RaiingPuddingUserRelation.KEY_PARENT_ID_INT, parentID);
//        values.put(UserMetadata.RaiingPuddingUserRelation.KEY_USER_ID_INT, userID);
//        values.put(UserMetadata.RaiingPuddingUserRelation.KEY_PROMISSION_ID_INT, promissionID);
//        values.put(UserMetadata.RaiingPuddingUserRelation.KEY_DATA_TYPE_INT, dataType);
//        //不能判断>0，没有值服务器返回-1，不保存数据库的话，数据库默认是0
//        values.put(UserMetadata.RaiingPuddingUserRelation.KEY_RELATION_INT, relation);
//        values.put(UserMetadata.RaiingPuddingUserRelation.KEY_STATUS_INT, status);
//        // pudding用户信息
//        long puddingUserRelationResult = db.insert(UserMetadata.RaiingPuddingUserRelation.TABLE_NAME_RPUR, null, values);
//        if (puddingUserRelationResult == -1) {
//            DetectLog.e(TAG + "======>创建pudding realtion用户失败");
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 删除指定UUID用户(匿名用户)的所有信息，包括通用信息,pudding专有信息和关良信息
//     * <p/> 注意:可登录用户不可以删除
//     *
//     * @param userUUID 用户的UUID
//     * @return 删除成功返回true。失败返回false
//     */
//    @Override
//    public synchronized boolean deleteAnonymousUser(String userUUID) {
//        if (TextUtils.isEmpty(userUUID)) {
//            DetectLog.d("传入的参数为空,userUUID为空");
//            return false;
//        }
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            boolean isSuccess = deleteUser(db, userUUID);
//            if (!isSuccess) {
//                return false;
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//        return true;
//    }
//
//    /**
//     * 删除登录用户,包括该用户下的所有的匿名用户
//     *
//     * @param uuid 登录用户的UUID
//     * @return 如果删除成功返回true, 失败返回false
//     */
//    @Override
//    public synchronized boolean deleteLoginUser(String uuid) {
//        if (TextUtils.isEmpty(uuid)) {
//            DetectLog.d("传入的参数为空,uuid为空");
//            return false;
//        }
//        if (uuid.contains("@")) {
//            // 因为之前传递的参数为邮箱，此处应该过滤到邮箱，如果是直接崩溃处理。只可能出现在开发阶段
//            throw new RuntimeException("传入的参数非法，不能传入邮箱，需要的是登录用户的UUID");
//        }
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            boolean re = deleteLoginUser(db, uuid);
//            if (!re) {
//                return false;
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//        return true;
//    }
//
//    /**
//     * 删除登录用户,包括该用户下的所有的匿名用户
//     *
//     * @param db         数据库
//     * @param createUUID 登录用户的UUID
//     * @return 删除成功，返回true
//     */
//    private boolean deleteLoginUser(SQLiteDatabase db, String createUUID) {
//        if (TextUtils.isEmpty(createUUID)) {
//            DetectLog.d("传入的用户UUID参数为空");
//            return false;
//        }
////        UserInfoDBEntity userInfoEntity = queryUserInfoByEmail(db, email);
//        UserInfoDBEntity userInfoEntity = queryUserInfoByUUID(db, createUUID);
//        if (userInfoEntity != null) {
//            String userUUID = userInfoEntity.getCommonUserInfo().getUuid();
//            ArrayList<UserInfoDBEntity> list = queryAllUserInfoByCreateUUID(db, userUUID);
//            for (int i = 0; i < list.size(); i++) {
//                UserInfoDBEntity entity = list.get(i);
//                String uuid = entity.getCommonUserInfo().getUuid();
//                boolean isSuccess = deleteUser(db, uuid);
//                if (!isSuccess) {
//                    return false;
//                }
//            }
//        } else {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 删除指定的uuid的用户
//     *
//     * @param db       用户数据库
//     * @param userUUID 用户的UUID
//     * @return 如果删除成功，返回true,失败返回false
//     */
//    private boolean deleteUser(SQLiteDatabase db, String userUUID) {
//        if (TextUtils.isEmpty(userUUID)) {
//            DetectLog.d("传入的用户UUID参数为空");
//            return false;
//        }
//        // 查询到用户的ID
//        int userID = queryUserIDByUUID(db, userUUID);
//        if (userID == -1) {
//            return false;
//        }
//        // 删除用户关系表中的关系
//        String tableName = UserMetadata.RaiingPuddingUserRelation.TABLE_NAME_RPUR;
//        String whereClause = UserMetadata.RaiingPuddingUserRelation.KEY_USER_ID_INT + "== ?";
//        String[] whereArgs = new String[]{userID + ""};
//        int re = db.delete(tableName, whereClause, whereArgs);
//        if (0 == re) {
//            return false;
//        }
//        // 删除pudding用户表中的信息
//        tableName = UserMetadata.RaiingPuddingUser.TABLE_NAME_RPU;
//        whereClause = UserMetadata.RaiingPuddingUser.KEY_USER_ID_INT + "== ?";
//        whereArgs = new String[]{userID + ""};
//        re = db.delete(tableName, whereClause, whereArgs);
//        if (0 == re) {
//            return false;
//        }
//        // 删除通用信息表中的内容
//        tableName = UserMetadata.RaiingUser.TABLE_NAME_RU;
//        whereClause = UserMetadata.RaiingUser.KEY_ID_INT + "== ?";
//        whereArgs = new String[]{userID + ""};
//        re = db.delete(tableName, whereClause, whereArgs);
//        return (0 != re);
//    }
//
//    /**
//     * 更新可登陆用户的信息
//     * <p/>注意:和更新匿名用户的区别在于用户关系
//     *
//     * @param uuid           用户的UUID
//     * @param nickName       用户的名称
//     * @param sex            用户的性别,1 -> 男,2-> 女,3->保密
//     * @param birthday       用户的生日,unix时间戳,单位s
//     * @param vaccination    是否预防接种,1-> 按时,2-> 未按时
//     * @param medicalHistory 是否有既往病史,1->是,2->否
//     * @return 更新成功返回true, 否者返回false
//     */
//    @Override
//    public synchronized boolean updateLoginUser(final String uuid,
//                                                final String email,
//                                                final String mobile,
//                                                final String nickName,
//                                                final String imageURL,
//                                                final int sex,
//                                                final long birthday,
//                                                final int vaccination,
//                                                final int medicalHistory) {
//        if (TextUtils.isEmpty(uuid) /*|| TextUtils.isEmpty(imageURL)*/) {
//            DetectLog.d("传入的参数为空,uuid为空");
//            return false;
//        }
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            boolean isSuccess = updateUser(db, uuid, email, mobile, nickName, imageURL, -1, sex, birthday, vaccination, medicalHistory);
//            if (!isSuccess) {
//                return false;
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//        return true;
//    }
//
//    /**
//     * 更新用户的头像
//     *
//     * @param uuid     用户的UUID
//     * @param imageURL 用户的头像的URL
//     * @return 如果更新成功返回true, 失败返回false
//     */
//    @Override
//    public synchronized boolean updateUserImageURL(String uuid, String imageURL) {
//        if (TextUtils.isEmpty(uuid) /*|| TextUtils.isEmpty(imageURL)*/) {
//            DetectLog.d("传入的参数为空,uuid为空");
//            return false;
//        }
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            UserInfoDBEntity entity = queryUserInfoByUUID(db, uuid);
//            if (entity == null) {
//                return false;
//            }
//            boolean isSuccess = updateUser(db, uuid, null, null, null, imageURL, -1, -1, UserConstant.BIRTHDAY_DEFAULT_VALUE, -1, -1);
//            if (!isSuccess) {
//                return false;
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//        return true;
//    }
//
//    /**
//     * 更新匿名用户
//     *
//     * @param uuid           用户的UUID
//     * @param nickName       用户的名称
//     * @param imageURL       用户头像的URL
//     * @param relation       和关联用户的关系，有10种关联关系,详见UserConstant
//     * @param sex            用户的性别,1 -> 男,2-> 女,3->保密
//     * @param birthday       用户的生日,unix时间戳,单位s
//     * @param vaccination    是否预防接种,1-> 按时,2-> 未按时
//     * @param medicalHistory 是否有既往病史,1->是,2->否
//     * @return 更新成功返回true, 否者返回false
//     */
//    @Override
//    public synchronized boolean updateAnonymousUser(String uuid,
//                                                    String nickName,
//                                                    String imageURL,
//                                                    int relation,
//                                                    int sex,
//                                                    long birthday,
//                                                    int vaccination,
//                                                    int medicalHistory) {
//        if (TextUtils.isEmpty(uuid)) {
//            DetectLog.d("传入的参数uuid为空");
//            return false;
//        }
//        if (TextUtils.isEmpty(nickName)) {
//            DetectLog.d("传入的参数昵称为空");
//            return false;
//        }
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            boolean isSuccess = updateUser(db, uuid, null, null, nickName, imageURL, relation, sex, birthday, vaccination, medicalHistory);
//            if (!isSuccess) {
//                return false;
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//        return true;
//    }
//
//    /**
//     * 更新用户信息,同时可以更新可登陆用户和匿名用户
//     *
//     * @param db             数据库
//     * @param uuid           用户的UUID，不能为空
//     * @param nickName       用户的名称
//     * @param imageURL       用户头像的URL
//     * @param relation       和关联用户的关系
//     * @param sex            用户的性别
//     * @param birthday       用户的生日,单位s
//     * @param vaccination    是否预防接种
//     * @param medicalHistory 是否有既往病史
//     * @return 更新成功返回true, 否者返回false
//     */
//    private boolean updateUser(final SQLiteDatabase db,
//                               final String uuid,
//                               final String email,
//                               final String mobile,
//                               final String nickName,
//                               final String imageURL,
//                               final int relation,
//                               final int sex,
//                               final long birthday,
//                               final int vaccination,
//                               final int medicalHistory) {
//        if (TextUtils.isEmpty(uuid)) {
//            DetectLog.d("用户的uuid不能为空");
//            return false;
//        }
//        // 查询出用户的ID
//        int userID = queryUserIDByUUID(db, uuid);
//        if (userID == -1) {
//            return false;
//        }
//        // 更新通用的用户信息表
//        String tableName = UserMetadata.RaiingUser.TABLE_NAME_RU;
//        ContentValues values = new ContentValues();
//        if (!TextUtils.isEmpty(email)) {
//            values.put(UserMetadata.RaiingUser.KEY_EMAIL_STR, email);
//        }
//        if (!TextUtils.isEmpty(mobile)) {
//            values.put(UserMetadata.RaiingUser.KEY_MOBILE_STR, mobile);
//        }
//        if (!TextUtils.isEmpty(nickName)) {
//            values.put(UserMetadata.RaiingUser.KEY_NICK_STR, nickName);
//        }
//        if (!TextUtils.isEmpty(imageURL)) {
//            values.put(UserMetadata.RaiingUser.KEY_USER_IMG_STR, imageURL);
//        }
//        // 只有当value中有值得时候，才执行更新操作
//        int commonValueSize = values.size();
//        if (commonValueSize > 0) {
//            String whereClause = UserMetadata.RaiingUser.KEY_ID_INT + " == ?";
//            String[] whereArgs = new String[]{userID + ""};
//            int re = db.update(tableName, values, whereClause, whereArgs);
//            if (0 == re) {
//                DetectLog.d(TAG + "===>更新数据库表，受到影响的行为0");
//                return false;
//            }
//        } else {
//            DetectLog.d(TAG + "===>需要更新通用用户信息表的value值个数为: " + values);
//        }
//
//        // 更新pudding用户表
//        tableName = UserMetadata.RaiingPuddingUser.TABLE_NAME_RPU;
//        values = new ContentValues();
//        values.put(UserMetadata.RaiingPuddingUser.KEY_BIRTHDAY_INT, birthday);
//        // }
////        sex 用户的性别, 1->男, 2->女, 3->保密
//        if (sex > 0) {
//            values.put(UserMetadata.RaiingPuddingUser.KEY_SEX_INT, sex);
//        }
////        vaccination    是否预防接种,1-> 按时,2-> 未按时
//        if (vaccination > 0) {
//            values.put(UserMetadata.RaiingPuddingUser.KEY_VACCINATION_INT, vaccination);
//        }
////        medicalHistory 是否有既往病史, 1->是, 2->否
//        if (medicalHistory > 0) {
//            values.put(UserMetadata.RaiingPuddingUser.KEY_MEDICAL_HISTORY_INT, medicalHistory);
//        }
//        // 需要更新的布丁用户信息表的大小
//        int puddingUserUpdateValueSize = values.size();
//        if (puddingUserUpdateValueSize > 0) {
//            String whereClause = UserMetadata.RaiingPuddingUser.KEY_USER_ID_INT + " == ?";
//            String[] whereArgs = new String[]{userID + ""};
//            int re = db.update(tableName, values, whereClause, whereArgs);
//            if (0 == re) {
//                DetectLog.d(TAG + "===>更新数据库表，受到影响的行为0");
//                return false;
//            }
//        } else {
//            DetectLog.d(TAG + "===>需要更新布丁用户信息表的value值个数为: " + values);
//        }
//        // 更新关系表
//        tableName = UserMetadata.RaiingPuddingUserRelation.TABLE_NAME_RPUR;
//        values = new ContentValues();
////      relation       和关联用户的关系，有10种关联关系,详见UserConstant
//        if (relation > 0) {
//            values.put(UserMetadata.RaiingPuddingUserRelation.KEY_RELATION_INT, relation);
//        }
//        // 关系表的待更新的value的大小
//        int relationValueSize = values.size();
//        if (relationValueSize > 0) {
//            String whereClause = UserMetadata.RaiingPuddingUserRelation.KEY_USER_ID_INT + " == ?";
//            String[] whereArgs = new String[]{userID + ""};
//            int re = db.update(tableName, values, whereClause, whereArgs);
//            if (0 == re) {
//                DetectLog.d(TAG + "===>更新数据库表，受到影响的行为0");
//                return false;
//            }
//        } else {
//            DetectLog.d(TAG + "===>需要更新关系表的value值个数为: " + values);
//        }
//        return true;
//    }
//
//    /**
//     * 根据登录用户的UUID，查询出所有的关联用户
//     *
//     * @param createUUID 可登陆用户的uuid
//     * @return 查询出来的所有用户，包括匿名用户和可登陆的用户,如果没有返回null
//     */
//    @Override
//    public synchronized List<UserInfoEntity> queryUserInfos(String createUUID) {
//        if (TextUtils.isEmpty(createUUID)) {
//            DetectLog.d("传入的参数为空,email为空");
//            return null;
//        }
//        if (createUUID.contains("@")) {
//            // 因为之前传递的参数为邮箱，此处应该过滤到邮箱，如果是直接崩溃处理。只可能出现在开发阶段
//            throw new RuntimeException("传入的参数非法，不能传入邮箱，需要的是登录用户的UUID");
//        }
//        DetectLog.e("查询用户信息时传入的用户邮箱为: " + createUUID);
//        ArrayList<UserInfoEntity> userInfos = null;
//        SQLiteDatabase db = null;
//        try {
//            db = mHelper.getWritableDatabase();
//            // 查询出可登陆用户的用户信息
////        UserInfoDBEntity entity = queryUserInfoByEmail(db, email);
//            UserInfoDBEntity entity = queryUserInfoByUUID(db, createUUID);
//            if (entity != null) {
//                // 需要判断该用户是否是登录用户
//                String uuid = entity.getCommonUserInfo().getUuid();
//                if (createUUID.equals(uuid)) {
//                    ArrayList<UserInfoDBEntity> list;
//                    userInfos = new ArrayList<>();
//                    list = queryAllUserInfoByCreateUUID(db, createUUID);
//                    for (int i = 0; i < list.size(); i++) {
//                        UserInfoEntity info = convertToUserInfoEntity(list.get(i));
//                        // 过滤掉没有被显式化的实体用户,匿名用户不管昵称是什么都显示
//                        String nickName = info.getNickName();
//                        if (Utils.isDefaultNickName(nickName) && info.getUuid().equals(createUUID)) {
//                            continue;
//                        }
//                        userInfos.add(info);
//                    }
//                } else {
//                    DetectLog.e(TAG + "===> 查询的不是登录用户的UUID: " + createUUID);
//                }
//
//            } else {
//                DetectLog.e(TAG + "===> 该邮箱下没有查询到任何关联的用户信息: " + createUUID);
//            }
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
//        return userInfos;
//    }
//
//    @Override
//    public synchronized UserInfoEntity queryUserInfo(String userUUID) {
//        SQLiteDatabase db = null;
//        try {
//            db = mHelper.getWritableDatabase();
//            // 查询出可登陆用户的用户信息
////        UserInfoDBEntity entity = queryUserInfoByEmail(db, email);
//            UserInfoDBEntity entity = queryUserInfoByUUID(db, userUUID);
//            if (entity != null) {
//                return convertToUserInfoEntity(entity);
//            } else {
//                DetectLog.e(TAG + "===> 该邮箱下没有查询到任何关联的用户信息: " + userUUID);
//            }
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 根据创建用户UUID，查询出所有关联的用户信息，包括自己本身
//     *
//     * @param db         用户数据库
//     * @param createUUID 可登陆用户的UUID
//     * @return 所有关联用户的集合，没有返回空集合
//     */
//    private ArrayList<UserInfoDBEntity> queryAllUserInfoByCreateUUID(SQLiteDatabase db, String createUUID) {
//        ArrayList<UserInfoDBEntity> list = new ArrayList<>();
//        String table = UserMetadata.RaiingUser.TABLE_NAME_RU;
//        String[] columns = new String[]{
//                UserMetadata.RaiingUser.KEY_ID_INT,
//                UserMetadata.RaiingUser.KEY_UUID_STR,
//                UserMetadata.RaiingUser.KEY_EMAIL_STR,
//                UserMetadata.RaiingUser.KEY_MOBILE_STR,
//                UserMetadata.RaiingUser.KEY_NICK_STR,
//                UserMetadata.RaiingUser.KEY_USER_IMG_STR,
//                UserMetadata.RaiingUser.KEY_SCORE_INT,
//                UserMetadata.RaiingUser.KEY_MONEY_INT,
//                UserMetadata.RaiingUser.KEY_EMAIL_ACTIVE_INT,
//                UserMetadata.RaiingUser.KEY_REMARK_STR
//        };
//        String selection = UserMetadata.RaiingUser.KEY_CREATE_UUID_STR + " == ?";
//        String[] selectionArgs = new String[]{createUUID};
////        String groupBy = null;
////        String having = null;
////        String orderBy = null;
//        Cursor cursor = null;
//        try {
//            cursor = db.query(table, columns, selection, selectionArgs,
//                    null, null, null);
//            while (cursor.moveToNext()) {
//                int userID = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_ID_INT));
//                String uuid = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_UUID_STR));
//                String email = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_EMAIL_STR));
//                String mobile = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_MOBILE_STR));
//                String nick = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_NICK_STR));
//                String userImgURL = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_USER_IMG_STR));
//                int score = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_SCORE_INT));
//                int money = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_MONEY_INT));
//                int emailActive = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_EMAIL_ACTIVE_INT));
//                String remark = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_REMARK_STR));
//                UserCommonDBEntity entity = new UserCommonDBEntity(userID, uuid, createUUID, email, mobile, nick, userImgURL, score, money, emailActive, remark);
//                // 查询pudding用户的信息
//                UserPuddingDBEntity puddingEntity = queryPuddingUserInfoByUserID(db, userID);
//                if (puddingEntity == null) {
//                    DetectLog.d(TAG + "===> 查询出来的布丁的用户信息为null");
//                    continue;
//                }
//                UserPuddingRelationDBEntity relationEntity = queryUserRelationInfoByUserID(db, userID);
//                if (relationEntity == null) {
//                    DetectLog.d(TAG + "===> 查询出来的关联的用户信息为null");
//                    continue;
//                }
//                list.add(new UserInfoDBEntity(entity, puddingEntity, relationEntity));
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return list;
//    }
//
//
//    /**
//     * 查询用户信息
//     *
//     * @param db   数据库
//     * @param uuid 用户的UUID
//     * @return 查找失败返回null
//     */
//    private UserInfoDBEntity queryUserInfoByUUID(SQLiteDatabase db, String uuid) {
//        int id = queryUserIDByUUID(db, uuid);
//        if (id == -1) {
//            return null;
//        }
//        UserInfoDBEntity entity = queryUserInfos(db, id);
//        if (entity == null) {
//            return null;
//        }
//        return entity;
//    }
//
//    /**
//     * 根据用户的UUID查询用户的ID
//     *
//     * @param db   数据库
//     * @param uuid 用户的UUID
//     *             <p/>注意不是创建用户的UUID,createUUID
//     * @return 返回查找到的用户的ID，如果查找没有找到或者查找失败返回-1
//     */
//    private int queryUserIDByUUID(SQLiteDatabase db, String uuid) {
//        int re = -1;
//        String table = UserMetadata.RaiingUser.TABLE_NAME_RU;
//        String[] columns = new String[]{UserMetadata.RaiingUser.KEY_ID_INT};
//        String selection = UserMetadata.RaiingUser.KEY_UUID_STR + " == ?";
//        String[] selectionArgs = new String[]{uuid};
////        String groupBy = null;
////        String having = null;
////        String orderBy = null;
//        Cursor cursor = null;
//        try {
//            cursor = db.query(table, columns, selection, selectionArgs,
//                    null, null, null);
//            int count = cursor.getCount();
//            if (count <= 0) {
//                DetectLog.e(TAG + "===> 没有找到对应uuid的用户，UUID: " + uuid);
//            } else if (count != 1) {
//                DetectLog.e(TAG + "===> 查询的用户的ID不唯一, uuid: " + uuid);
//            } else {
//                if (cursor.moveToNext()) {
//                    re = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_ID_INT));
//                }
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return re;
//    }
//
//    /**
//     * 查询出用户的信息,包括所有的用户的信息,包括关联信息
//     *
//     * @param db     用户数据库
//     * @param userID 用户的ID
//     * @return 返回该用户下所有的用户信息，没有返回null
//     */
//    private UserInfoDBEntity queryUserInfos(SQLiteDatabase db, int userID) {
//        // 查询出通用的用户信息
//        UserCommonDBEntity commonUserInfo = queryCommonUserInfoByUserID(db, userID);
//        if (commonUserInfo == null) {
//            DetectLog.e(TAG + "=====>查询通用用户信息为null");
//            return null;
//        }
//        //  查询布丁专有的用户信息
//        UserPuddingDBEntity puddingUserInfo = queryPuddingUserInfoByUserID(db, userID);
//        if (puddingUserInfo == null) {
//            DetectLog.e(TAG + "=====>查询布丁用户信息为null");
//            return null;
//        }
//        UserPuddingRelationDBEntity relationUserInfo = queryUserRelationInfoByUserID(db, userID);
//        if (relationUserInfo == null) {
//            DetectLog.e(TAG + "=====>查询布丁用户信息为null");
//            return null;
//        }
//        // 返回联合的用户信息
//        return new UserInfoDBEntity(commonUserInfo, puddingUserInfo, relationUserInfo);
//    }
//
//    /**
//     * 查询出布丁用户的信息
//     *
//     * @param db     用户数据库
//     * @param userID 用户的ID
//     * @return 返回布丁用户的信息，对应raiing_pudding表中的全部字段。如果没有返回null
//     */
//    private UserPuddingDBEntity queryPuddingUserInfoByUserID(SQLiteDatabase db, int userID) {
//        UserPuddingDBEntity entity = null;
//        String table = UserMetadata.RaiingPuddingUser.TABLE_NAME_RPU;
//        // 布丁用户信息中，除UserID外的所有的字段
//        String[] columns = new String[]{
//                UserMetadata.RaiingPuddingUser.KEY_BIRTHDAY_INT,
//                UserMetadata.RaiingPuddingUser.KEY_SEX_INT,
//                UserMetadata.RaiingPuddingUser.KEY_VACCINATION_INT,
//                UserMetadata.RaiingPuddingUser.KEY_MEDICAL_HISTORY_INT
//        };
//        String selection = UserMetadata.RaiingPuddingUser.KEY_USER_ID_INT + " == ?";
//        String[] selectionArgs = new String[]{userID + ""};
////        String groupBy = null;
////        String having = null;
////        String orderBy = null;
//        Cursor cursor = null;
//        try {
//            cursor = db.query(table, columns, selection, selectionArgs,
//                    null, null, null);
//            if (cursor.moveToNext()) {
//                long birthday = cursor.getLong(cursor.getColumnIndex(UserMetadata.RaiingPuddingUser.KEY_BIRTHDAY_INT));
//                int sex = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingPuddingUser.KEY_SEX_INT));
//                int vaccination = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingPuddingUser.KEY_VACCINATION_INT));
//                int medicalHistory = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingPuddingUser.KEY_MEDICAL_HISTORY_INT));
//                // 填充查询出来的用户信息
//                entity = new UserPuddingDBEntity(userID, birthday, sex, vaccination, medicalHistory);
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return entity;
//    }
//
//
//    /**
//     * 查询通用的用户信息
//     *
//     * @param db     用户数据库
//     * @param userID 用户的ID
//     * @return 返回通用的用户信息，包含全部的字段。如果没有查询到，返回null
//     */
//    private UserCommonDBEntity queryCommonUserInfoByUserID(SQLiteDatabase db, int userID) {
//        // 查询出来的通用用户信息
//        UserCommonDBEntity entity = null;
//
//        String table = UserMetadata.RaiingUser.TABLE_NAME_RU;
//        // 通用用户信息中，除UserID外的所有的字段
//        String[] columns = new String[]{
//                UserMetadata.RaiingUser.KEY_UUID_STR, // 用户的uuid
//                UserMetadata.RaiingUser.KEY_CREATE_UUID_STR,
//                UserMetadata.RaiingUser.KEY_EMAIL_STR,
//                UserMetadata.RaiingUser.KEY_MOBILE_STR,
//                UserMetadata.RaiingUser.KEY_NICK_STR,
//                UserMetadata.RaiingUser.KEY_USER_IMG_STR,
//                UserMetadata.RaiingUser.KEY_SCORE_INT,
//                UserMetadata.RaiingUser.KEY_MONEY_INT,
//                UserMetadata.RaiingUser.KEY_EMAIL_ACTIVE_INT,
//                UserMetadata.RaiingUser.KEY_REMARK_STR
//        };
//        String selection = UserMetadata.RaiingUser.KEY_ID_INT + " == ?";
//        String[] selectionArgs = new String[]{userID + ""};
////        String groupBy = null;
////        String having = null;
////        String orderBy = null;
//        Cursor cursor = null;
//        try {
//            cursor = db.query(table, columns, selection, selectionArgs,
//                    null, null, null);
//            if (cursor.moveToNext()) {
//                String uuid = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_UUID_STR));
//                String createUUID = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_CREATE_UUID_STR));
//                String email = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_EMAIL_STR));
//                String mobile = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_MOBILE_STR));
//                String nick = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_NICK_STR));
//                String userImgURL = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_USER_IMG_STR));
//                int score = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_SCORE_INT));
//                int money = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_MONEY_INT));
//                int emailActive = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_EMAIL_ACTIVE_INT));
//                String remark = cursor.getString(cursor.getColumnIndex(UserMetadata.RaiingUser.KEY_REMARK_STR));
//                // 填充查询出来的用户信息
//                entity = new UserCommonDBEntity(userID, uuid, createUUID, email, mobile, nick, userImgURL, score, money, emailActive, remark);
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return entity;
//    }
//
//    /**
//     * 根据用户的ID，查询关联关系
//     *
//     * @param db     用户数据库
//     * @param userID 用户的ID
//     * @return 查询出来的用户关系，没有返回null
//     */
//    private UserPuddingRelationDBEntity queryUserRelationInfoByUserID(SQLiteDatabase db, int userID) {
//        UserPuddingRelationDBEntity entity = null;
//        String table = UserMetadata.RaiingPuddingUserRelation.TABLE_NAME_RPUR;
//        // 布丁用户信息中，除UserID外的所有的字段
//        String[] columns = new String[]{
//                UserMetadata.RaiingPuddingUserRelation.KEY_PARENT_ID_INT,
//                UserMetadata.RaiingPuddingUserRelation.KEY_DATA_TYPE_INT,
//                UserMetadata.RaiingPuddingUserRelation.KEY_PROMISSION_ID_INT,
//                UserMetadata.RaiingPuddingUserRelation.KEY_RELATION_INT,
//                UserMetadata.RaiingPuddingUserRelation.KEY_STATUS_INT
//        };
//        String selection = UserMetadata.RaiingPuddingUserRelation.KEY_USER_ID_INT + " == ?";
//        String[] selectionArgs = new String[]{userID + ""};
////        String groupBy = null;
////        String having = null;
////        String orderBy = null;
//        Cursor cursor = null;
//        try {
//            cursor = db.query(table, columns, selection, selectionArgs,
//                    null, null, null);
//            if (cursor.moveToNext()) {
//                int parentID = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingPuddingUserRelation.KEY_PARENT_ID_INT));
//                int dataType = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingPuddingUserRelation.KEY_DATA_TYPE_INT));
//                int promissionID = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingPuddingUserRelation.KEY_PROMISSION_ID_INT));
//                int relation = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingPuddingUserRelation.KEY_RELATION_INT));
//                int status = cursor.getInt(cursor.getColumnIndex(UserMetadata.RaiingPuddingUserRelation.KEY_STATUS_INT));
//                // 填充查询出来的用户关系
//                entity = new UserPuddingRelationDBEntity(userID, parentID, dataType, promissionID, relation, status);
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return entity;
//    }
//
//
//    /**
//     * 转化实体类
//     *
//     * @param entity 数据库操作实体类
//     * @return 对外暴露的用户信息实体类
//     */
//    private UserInfoEntity convertToUserInfoEntity(UserInfoDBEntity entity) {
//        String email = entity.getCommonUserInfo().getEmail();
//        String mobile = entity.getCommonUserInfo().getMobile();
//        String photoURL = entity.getCommonUserInfo().getUserImgURL();
//        String name = entity.getCommonUserInfo().getNick();
//        String createUUID = entity.getCommonUserInfo().getCreateUuid();
//        String vUUID = entity.getCommonUserInfo().getUuid();
//        int sex = entity.getPuddingUserInfo().getSex();
//        long birthday = entity.getPuddingUserInfo().getBirthday();
//        int relation = entity.getRelationUserInfo().getRelation();
//        int permissionID = entity.getRelationUserInfo().getPermissionID();
//        int vaccine = entity.getPuddingUserInfo().getVaccination();
//        int condition = entity.getPuddingUserInfo().getMedicalHistory();
//        int status = entity.getRelationUserInfo().getStatus();
//        int activeEmail = entity.getCommonUserInfo().getEmailActive();
////       public UserInfoEntity(String email, String photoPath, String nickName, String uuid, String vuuid, int sex, int relation, int birthday,
//// int vaccine, int condition, int status, int permissionID, int activeEmail)
//        return new UserInfoEntity(email, mobile, photoURL, name, vUUID, createUUID, sex, relation, birthday, vaccine, condition, status, permissionID, activeEmail);
//    }
//
//}