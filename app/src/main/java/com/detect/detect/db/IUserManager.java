//package com.detect.detect.db;
//
//import com.raiing.pudding.data.UserInfoEntity;
//
//import java.util.List;
//
///**
// * @author jun.wang@raiing.com.
// * @version 1.1 2015/8/28
// */
//public interface IUserManager {
//
//
//    /**
//     * 创建可以登录的用户，用户的邮箱和手机号，两者至少要有一个存在
//     *
//     * @param uuid        用户的UUID
//     * @param email       用户的email
//     * @param mobile      用户的手机号
//     * @param nickName    用户昵称，之前邮箱注册不需要输入用户昵称，现在无论是手机注册还是邮箱注册都需要输入用户昵称
//     * @param imageURL    用户头像的URL,没有可以设置为null或者""
//     * @param emailActive 用户邮箱的状态,1->激活,2->未激活
//     * @return 创建成功，返回true。否者返回false
//     */
//    boolean createLoginUser(String uuid, String email, String mobile, String nickName, String imageURL, int emailActive);
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
//     * @param birthday       用户的生日,unix时间戳,单位s. 1970年之前为负值
//     * @param vaccination    疫苗接种情况,1-按时,2-未按时
//     * @param medicalHistory 是否有既往病史,1-是, 2-否
//     * @return 创建成功，返回true。否者返回false
//     */
//    boolean createAnonymousUser(String uuid, String createUUID, String nickName, String imageURL,
//                                int relation, int sex, long birthday, int vaccination, int medicalHistory);
//
//
//    /**
//     * 保存从服务器下载下来的所有用户
//     *
//     * @param entitys 用户的集合
//     * @return 保存成功返回true
//     */
//    boolean saveAllUserFromServer(List<UserInfoEntity> entitys);
//
//    /**
//     * 删除登录用户,包括该用户下的所有的匿名用户
//     *
//     * @param uuid 登录用户的UUID
//     * @return 如果删除成功返回true, 失败返回false
//     */
//    boolean deleteLoginUser(String uuid);
//
//
//    /**
//     * 删除指定UUID用户(匿名用户)的所有信息，包括通用信息,pudding专有信息和关良信息
//     * <p/> 注意:可登录用户不可以删除
//     *
//     * @param userUUID 用户的UUID
//     * @return 删除成功返回true。失败返回false
//     */
//    boolean deleteAnonymousUser(String userUUID);
//
//
////    /**
////     * 根据登录用户的UUID，查询出所有的关联用户
////     *
////     * @param email 可登陆用户的邮箱
////     * @return 查询出来的所有用户，包括匿名用户和可登陆的用户,如果没有返回null
////     */
////    List<UserInfoEntity> queryUserInfos(String email);
//
//    /**
//     * 根据登录用户的UUID，查询出所有的关联用户
//     *
//     * @param createUUID 登录用户的UUID
//     * @return 查询出来的所有用户，包括匿名用户和可登陆的用户,如果没有返回null
//     */
//    List<UserInfoEntity> queryUserInfos(String createUUID);
//
//    /**
//     * 根据用户UUID，查询出对应的用户信息
//     *
//     * @param userUUID 用户的UUID
//     * @return 查询出来的用户，可能是匿名用户和登陆的用户,如果没有返回null
//     */
//    UserInfoEntity queryUserInfo(String userUUID);
//
//    /**
//     * 更新用户的头像
//     *
//     * @param uuid     用户的UUID
//     * @param imageURL 用户的头像的URL
//     * @return 如果更新成功返回true, 失败返回false
//     */
//    boolean updateUserImageURL(String uuid, String imageURL);
//
//
//    /**
//     * 更新匿名用户
//     *
//     * @param uuid           用户的UUID
//     * @param nickName       用户的名称
//     * @param imageURL       用户头像的URL
//     * @param relation       和关联用户的关系，有10种关联关系,详见UserConstant
//     * @param sex            用户的性别,1 -> 男,2-> 女,3->保密
//     * @param birthday       用户的生日,unix时间戳,单位s， 1970年之前为负值
//     * @param vaccination    是否预防接种,1-> 按时,2-> 未按时
//     * @param medicalHistory 是否有既往病史,1->是,2->否
//     * @return 更新成功返回true, 否者返回false
//     */
//    boolean updateAnonymousUser(String uuid, String nickName, String imageURL, int relation, int sex, long birthday, int vaccination, int medicalHistory);
//
//
//    /**
//     * 更新可登陆用户的信息，如果是手机号注册，可以关联邮箱，如果是邮箱注册，可以绑定手机号。
//     * 可以更新特定的信息，不需要更新的字段可以写入非法值，或者置为空。
//     * 比如： 更新仅更新邮箱时，可以把mobile，nickname， imageURL置为null，sex，birthday，vaccination，medicalHistory可以传入小于0的非法值。
//     * 考虑到组合的方式太多，暂不对不同的类型数据更新单独定义不同的接口
//     * <p/>
//     * <p/>注意:和更新匿名用户的区别在于用户关系
//     *
//     * @param uuid           用户的UUID， 不能为空
//     * @param email          更新用户的邮箱。
//     * @param mobile         更新用户的手机号，
//     * @param nickName       用户的名称
//     * @param imageURL       头像的URL
//     * @param sex            用户的性别,1 -> 男,2-> 女,3->保密
//     * @param birthday       用户的生日,unix时间戳,单位s，1970年之前为负值
//     * @param vaccination    是否预防接种,1-> 按时,2-> 未按时
//     * @param medicalHistory 是否有既往病史,1->是,2->否
//     * @return 更新成功返回true, 否者返回false
//     */
//    boolean updateLoginUser(String uuid, String email, String mobile, String nickName, String imageURL, int sex, long birthday, int vaccination, int medicalHistory);
//
//
//}
