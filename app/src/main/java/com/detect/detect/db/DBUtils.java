//package com.detect.detect.db;
//
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.text.TextUtils;
//
//import com.detect.detect.constant.FileConstant;
//import com.detect.detect.log.DetectLog;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.ArrayList;
//
///**
// * 数据库操作相关的工具类
// *
// * @author jun.wang@raiing.com.
// * @version 1.1 2015/8/28
// */
//public class DBUtils {
//    /**
//     * 格式化操作日志为JSON字符串
//     *
//     * @param type 事件的操作类型，创建，更新，删除
//     * @param time 事件的时间
//     * @param uuid 事件的UUID
//     * @param name 事件的名称
//     * @param info 事件的内容,可以为空
//     * @return 返回格式后的JSON字符串
//     * @throws JSONException
//     */
//    public static JSONObject formatOpData(int type, int time, String uuid,
//                                          String name, String info) throws JSONException {
//        JSONObject op = new JSONObject();
//        // "c" : “RVMEvent”, 事件类型
//        // "t" : uinx时间戳, 事件中对外暴露的时间戳(事件发生时的时间，用户指定的时间或者是当前时间)
//        // "p" :
//        // text,json字符串，如{"menstr_cyc":"xxxxxxx","name":"xxxxxxxx","info":"xxxxxxx"}
//        JSONObject ev = new JSONObject();
//        JSONObject p = new JSONObject();
//        JSONObject infoOb;
//        p.put(DataConstant.KEY_EVENT_NAME, name);
//        // info字段可能为空,因为在解析的时候，没有这个字段，填入的为""。所有此处需要增加""的判断
//        if (!TextUtils.isEmpty(info)) {
//            infoOb = new JSONObject(info);
//            p.put(DataConstant.KEY_EVENT_INFO, infoOb.toString());
//        }
//        ev.put(DataConstant.KEY_EVENT_TYPE, DataConstant.VALUE_EVENT_TYPE1);
//        ev.put(DataConstant.KEY_EVENT_TIME, time);
//        ev.put(DataConstant.KEY_EVENT_PROP, p);
//
//        op.put(DataConstant.KEY_EVENT_OP, type);
//        op.put(DataConstant.KEY_EVENT_UUID, uuid);
//        op.put(DataConstant.KEY_EVENT_CONTENT, ev);
//        return op;
//    }
//
//    /**
//     * 解析ops->事件的实体. 需要进行特殊转换的操作,直接在这里进行
//     *
//     * @param ops 事件的操作日志
//     * @return 解析后的事件实体对象
//     * @throws JSONException
//     */
//    public static OpsParseEntity parseEventOps(JSONObject ops) throws JSONException {
////        DetectLog.d("解析的原始的ops: " + ops.toString());
////        JSONObject object = new JSONObject(ops);
//        int op = ops.getInt(DataConstant.KEY_EVENT_OP);
//        String uuid = ops.getString(DataConstant.KEY_EVENT_UUID);
//        boolean isContainLowerCase = Utils.isContainLowerCase(uuid);
//        if (isContainLowerCase) {
//            // 如果下载下来为小写的UUID,统一转化为大写
//            uuid = uuid.toUpperCase();
//        }
//        JSONObject ev = ops.getJSONObject(DataConstant.KEY_EVENT_CONTENT);
//        int time = ev.getInt(DataConstant.KEY_EVENT_TIME);
//        JSONObject p = ev.getJSONObject(DataConstant.KEY_EVENT_PROP);
//        String name = p.getString(DataConstant.KEY_EVENT_NAME);
//        // 有可能没有info字段。冷敷事件没有info字段
//        String info = p.optString(DataConstant.KEY_EVENT_INFO);
//        // 对事件的名称进行过滤,无法识别的事件,直接忽略
//        switch (name) {
//            // 旧的药品名,替换为新的方法名
//            case DataConstant.MEDICINE_EVENT_NAME_OLD:
//                // 替换为新的药品名
//                name = DataConstant.MEDICINE_EVENT_NAME;
//                // 解析info字段
//                if (info.contains(DataConstant.KEY_OLD_MEDICINE_EVENT_INFO)) {
//                    JSONObject obj = new JSONObject(info);
//                    String medicine = obj.getString(DataConstant.KEY_OLD_MEDICINE_EVENT_INFO);
//                    JSONObject infoObj = formatMedicines(medicine);
//                    if (infoObj != null) {
//                        // 转换为新的格式进行存储
//                        info = infoObj.toString();
//                    }
//                }
//                break;
//            case DataConstant.MEDICINE_EVENT_NAME:
//                // 新的吃药事件
//                break;
//            case DataConstant.COLD_EVENT_NAME:
//                // 冷敷名称
//                break;
//            case DataConstant.CUSTOM_EVENT_NAME:
//                // 自定义事件
//                break;
//            default:
//                // 无法识别的事件,直接返回
//                DetectLog.d("无法解析的事件名称,直接返回: " + name);
//                return null;
//        }
//        return new OpsParseEntity(time, name, info, uuid, op);
//    }
//
//    /**
//     * 格式化药品名
//     *
//     * @param medicineNames 药品集合
//     * @return 格式化的药品信息
//     * @throws JSONException
//     */
//    public static JSONObject formatMedicines(String... medicineNames) throws JSONException {
//        if (medicineNames == null) {
//            DetectLog.d("传入的药名的名称为空");
//            return null;
//        }
//        JSONArray medicines = new JSONArray();
//        // 事件的内容
//        for (String name : medicineNames) {
//            medicines.put(name);
//        }
//        JSONObject info = new JSONObject();
//        info.put(DataConstant.KEY_MEDICINE_EVENT_INFO, medicines);
//        return info;
//    }
//
//    /**
//     * 解析从服务端下载的用户的数据
//     *
//     * @param data 用户的数据
//     * @return 解析后的用户集合, 出错返回null
//     */
//    public static List<UserInfoEntity> parseUserData(JSONArray data, String createUUID) throws JSONException {
//        if (TextUtils.isEmpty(createUUID)) {
//            DetectLog.e("传入的实体用户的UUID为空");
//            return null;
//        }
//        if (data == null) {
//            DetectLog.d("传入的待解析的数据为空");
//            return null;
//        }
//        List<UserInfoEntity> list = new ArrayList<>();
//        for (int i = 0; i < data.length(); i++) {
//            JSONObject object1 = data.getJSONObject(i);
////                DetectLog.d("login-->>用户信息-->>object1.length()" + object1.toString());
//            String user_type = object1.getString("user_type");
//            // v2_reg新注册用户，v2_data旧用户
//            if (user_type.equals("v2_reg") || user_type.equals("v2_data") || user_type.equals("v3_reg_wx")) {
//                long birthday = object1.optLong("birthday", -1);
//                int relation_id = object1.getInt("relation_id");
//                int sex = object1.getInt("sex");
//                int medical_history = object1.getInt("medical_history");
////                    int weight = object1.getInt("weight");
////                    int height = object1.getInt("height");
//                int permission_id = object1.getInt("permission_id");
//                String nick = object1.getString("nick");
//                int status = object1.getInt("status");
//                String v_uuid = object1.getString("v_uuid");
//                // 不确定是否有这个实体用户的UUID
////                String uuid = object1.getString("uuid");
////                    String blood_type = object1.getString("blood_type");
//                int vaccination = object1.getInt("vaccination");
//                String user_img = object1.getString("user_img");
////                DetectLog.d("用户名称： " + nick + " ，获取到的用户的头像为: " + user_img);
//                // 当服务端头像不存在的时候，默认会返回下面的字符串
//                if (user_img.equals("userImg")) {
//                    user_img = "";
//                }
//                // 实体用户的邮箱
//                String email = object1.optString("email");
//                //中国注册逻辑取消邮箱后，服务端会返回null 所以此处加上邮箱的匹配验证，为了不影响后续逻辑的非空判断
//                if (!Utils.isEmail(email)) {
//                    email = "";
//                }
//                // 用户的手机号
//                String mobile = object1.optString("mobile");
//                // 邮箱是否激活
//                int activeEmail = object1.optInt("email_active");
//// public UserInfoEntity(String photoPath, String nickName, String uuid, String vuuid, int sex, int relation, int birthday, int vaccine, int condition, int status, int permissionID) {
//                list.add(new UserInfoEntity(email, mobile, user_img, nick, v_uuid, createUUID, sex, relation_id, birthday, vaccination, medical_history, status, permission_id, activeEmail));
//            }
//        }
//        return list;
//    }
//
//    /**
//     * 判断某个表是否存在
//     *
//     * @param db        数据库
//     * @param tableName 表名
//     * @return 存在true 不存在false
//     */
//    public static boolean tabbleIsExist(SQLiteDatabase db, String tableName) {
//        boolean result = false;
//        if (tableName == null) {
//            return false;
//        }
//        Cursor cursor = null;
//        try {
//            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";
//            cursor = db.rawQuery(sql, null);
//            if (cursor.moveToNext()) {
//                int count = cursor.getInt(0);
//                if (count > 0) {
//                    result = true;
//                }
//            }
//
//        } catch (Exception e) {
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 是否需要copy数据
//     *
//     * @param userUUID
//     * @param tableName 表名
//     * @return 需要true 不需要false
//     */
//    public static boolean isNeedCopy(String userUUID, String tableName) {
//        boolean isNeed = false;
//        String dbPath = FileConstant.DB_PATH + userUUID + ".db";
//        File file = new File(dbPath);
//        if (file.exists()) {
//            //获取旧数据库路径
//            DatabaseContext dbContext1 = new DatabaseContext(RaiingApplication.context);
//            //获取旧数据库
//            DataDBHelper dbHelper = new DataDBHelper(dbContext1, userUUID + ".db");
//            SQLiteDatabase db = dbHelper.getReadableDatabase();
//            //检查表是否存在，存在就复制数据，不存在就不管了
//            isNeed = tabbleIsExist(db, tableName);
//        }
//        return isNeed;
//    }
//
//    /**
//     * Ops字符串对应的解析后的实体类
//     */
//    public static class OpsParseEntity {
//        /**
//         * ops中解析出来的事件的时间，单位s
//         */
//        public int time;
//        /**
//         * 事件的名称
//         */
//        public String name;
//        /**
//         * 事件的信息，可以为""
//         */
//        public String info;
//        /**
//         * 事件的UUID
//         */
//        public String uuid;
//        /**
//         * 事件的操作类型
//         */
//        public int op;
//
//        public OpsParseEntity(int time, String name, String info, String uuid, int op) {
//            this.time = time;
//            this.name = name;
//            this.info = info;
//            this.uuid = uuid;
//            this.op = op;
//        }
//
//        public int getTime() {
//            return time;
//        }
//
//        public void setTime(int time) {
//            this.time = time;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getInfo() {
//            return info;
//        }
//
//        public void setInfo(String info) {
//            this.info = info;
//        }
//
//        public String getUuid() {
//            return uuid;
//        }
//
//        public void setUuid(String uuid) {
//            this.uuid = uuid;
//        }
//
//        public int getOp() {
//            return op;
//        }
//
//        public void setOp(int op) {
//            this.op = op;
//        }
//
//        @Override
//        public String toString() {
//            return "OpsParseEntity{" +
//                    "time=" + time +
//                    ", name='" + name + '\'' +
//                    ", info='" + info + '\'' +
//                    ", uuid='" + uuid + '\'' +
//                    ", op=" + op +
//                    '}';
//        }
//    }
//
//}
