//package com.detect.detect.shared_preferences;
//
//import android.content.Context;
//
//import java.util.List;
//
//import static android.content.ContentValues.TAG;
//
///**
// * Created by yu on 18/1/20.
// */
//
//public class DataManager implements IProjectDataManager {
//    private static Context sContext;
//    private static DataManager mDataManager;
//    private static IProjectDataManager mProjectDataManager;
//
//    /**
//     * 初始化
//     *
//     * @param context 全局的context，在Application中进行初始化
//     */
//    public static void initialize(Context context, IProjectDataManager projectDataManager) {
//        mProjectDataManager = projectDataManager;
//        sContext = context;
//    }
//
//
//    public static DataManager getInstance() {
//        if (sContext == null || mProjectDataManager == null) {
//            throw new IllegalArgumentException(TAG + "必须先调用initialize方法初始化为全局的Context对象");
//        }
//        if (null == mDataManager) {
//            synchronized (DataManager.class) {
//                if (null == mDataManager) {
//                    mDataManager = new DataManager();
//                }
//            }
//        }
//        return mDataManager;
//    }
//
//    @Override
//    public void insertTestPoint(String projectName, TestPoint testPoint) {
//        mProjectDataManager.insertTestPoint(projectName, testPoint);
//    }
//
//    @Override
//    public List<Project> getAllProjects() {
//        return mProjectDataManager.getAllProjects();
//    }
//
//    @Override
//    public List<String> getAllProjectNames() {
//        return mProjectDataManager.getAllProjectNames();
//    }
//
//    @Override
//    public boolean isProjectNameExit(String projectName) {
//        return mProjectDataManager.isProjectNameExit(projectName);
//    }
//
//    @Override
//    public int getMaxBuildSerialNum(String projectName) {
//        return mProjectDataManager.getMaxBuildSerialNum(projectName);
//    }
//
//    @Override
//    public String getTestPointPicPath(String projectName, int buildSerialNum) {
//        return mProjectDataManager.getTestPointPicPath(projectName, buildSerialNum);
//    }
//
//    @Override
//    public boolean deleteTestPoint(String projectName, int buildSerialNum) {
//        return mProjectDataManager.deleteTestPoint(projectName, buildSerialNum);
//    }
//
//    @Override
//    public boolean deleteProject(String projectName) {
//        return mProjectDataManager.deleteProject(projectName);
//    }
//}
