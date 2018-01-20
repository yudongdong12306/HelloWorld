package com.detect.detect.db;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理多个用户的数据 数据库
 * Created by jun on 15/7/23.
 */
public class MultiDataDBManager {

    private static Context mContext;

    /**
     * 保存多个用户数据库的管理对象
     * <pre>
     *      map中的
     *      key: 数据库的名称，用户的uuid
     *      value: 对应的数据库管理对象
     * </pre>
     */
    private static Map<String, IDataManager> mManagers = new HashMap<>();

    /**
     * 初始化
     *
     * @param context 全局的context，在applacation中进行初始化
     */
    public static void initialize(Context context) {
        mContext = context;
    }

    /**
     * 根据数据库的名称，获取对应的数据库对象
     *
     * @param dbName 数据库的名称，用户的UIUD
     * @return 返回对应的数据库，
     */
    public static IDataManager getDataDBManager(String dbName) {
        if ((mContext == null) || !(mContext instanceof Application)) {
            throw new IllegalArgumentException("必须先调用initialize方法初始化为全局的Context对象");
        }
        if (TextUtils.isEmpty(dbName)) {
            throw new IllegalArgumentException("数据库的名称不能为空，目前为用户的UUID");
        }
        IDataManager db = mManagers.get(dbName);
        if (db == null) {
            synchronized (MultiDataDBManager.class) {
                db = mManagers.get(dbName);
                if (db == null) {
                    db = new DataDBManager(mContext, dbName);
                    // 加入到集合中进行管理
                    mManagers.put(dbName, db);
                }
            }
        }
        return db;
    }
}
