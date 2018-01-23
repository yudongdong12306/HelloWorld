//package com.detect.detect.db;
//
//import android.app.Application;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.text.TextUtils;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 管理多个用户的数据 数据库
// * Created by jun on 15/7/23.
// */
//public class MultiDataDBManager {
//
//    private static Context mContext;
//
//    /**
//     * 保存多个用户数据库的管理对象
//     * <pre>
//     *      map中的
//     *      key: 数据库的名称，用户的uuid
//     *      value: 对应的数据库管理对象
//     * </pre>
//     */
//    private static Map<String, IDataManager> mManagers = new HashMap<>();
//
//    public static DatabaseContext getDbContext() {
//        return mDbContext;
//    }
//
//    private static DatabaseContext mDbContext;
//
//    /**
//     * 初始化
//     *
//     * @param context 全局的context，在applacation中进行初始化
//     */
//    public static void initialize(Context context) {
//        mContext = context;
//        mDbContext = new DatabaseContext(context);
//    }
//
//    /**
//     * 根据数据库的名称，获取对应的数据库对象
//     *
//     * @param projectName 数据库的名称，用户的UIUD
//     * @return 返回对应的数据库，
//     */
//    public static IDataManager getDataDBManager(String projectName) {
//        if ((mContext == null) || !(mContext instanceof Application)) {
//            throw new IllegalArgumentException("必须先调用initialize方法初始化为全局的Context对象");
//        }
//        if (TextUtils.isEmpty(projectName)) {
//            throw new IllegalArgumentException("数据库的名称不能为空，目前为用户的UUID");
//        }
//        IDataManager db = mManagers.get(projectName);
//        if (db == null) {
//            synchronized (MultiDataDBManager.class) {
//                db = mManagers.get(projectName);
//                if (db == null) {
//                    db = new DataDBManager(mDbContext, projectName);
//                    // 加入到集合中进行管理
//                    mManagers.put(projectName, db);
//                }
//            }
//        }
//        return db;
//    }
//
//    public List<String> getTableNameList() {
//        SQLiteDatabase database = mDbContext.openOrCreateDatabase(DbConstant.TEMPERATURE_DB, Context.MODE_PRIVATE, null);
//        ArrayList<String> tableNameList = new ArrayList<>();
//        Cursor cursor = null;
//        try {
//            String sql = " SELECT NAME FROM SQLITE_MASTER WHERE TYPE='TABLE' ORDER BY NAME ";
//
//            cursor = database.rawQuery(sql, null);
//            while (cursor.moveToNext()) {
//                String tableName = cursor.getString(0);
//                if (!TextUtils.isEmpty(tableName)) {
//                    tableNameList.add(tableName);
//                }
//            }
//            return tableNameList;
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
////            database.close();
//        }
//    }
//}
