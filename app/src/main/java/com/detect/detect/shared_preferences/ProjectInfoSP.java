package com.detect.detect.shared_preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.detect.detect.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by damon on 3/7/16.
 * 存储好孕墙相关的缓存
 */
public class ProjectInfoSP {
    private static final String TAG = "ProjectInfoSP...";
    /**
     * shared公共存储的文件名字
     */
    private static final String SHARED_NAME_PUBLIC = "shared_name_public";

    /**
     * 工程的key
     */
    private static final String SHARED_KEY_PROJECT_INFO = "project_info";

    private static Context sContext;
    private static ProjectInfoSP sSharedPrefHelper;
    private SharedPreferences preferences;

    private ProjectInfoSP() {
        preferences = sContext.getSharedPreferences(SHARED_NAME_PUBLIC, Context.MODE_PRIVATE);
    }


    /**
     * 初始化
     *
     * @param context 全局的context，在Application中进行初始化
     */
    public static void initialize(Context context) {
        sContext = context;
    }


    public static ProjectInfoSP getInstance() {
        if ((sContext == null)) {
            throw new IllegalArgumentException(TAG + "必须先调用initialize方法初始化为全局的Context对象");
        }
        if (null == sSharedPrefHelper) {
            synchronized (ProjectInfoSP.class) {
                if (null == sSharedPrefHelper) {
                    sSharedPrefHelper = new ProjectInfoSP();
                }
            }
        }
        return sSharedPrefHelper;
    }

    /**
     * 新建一个项目
     *
     * @param projectName 项目名称
     * @param testPoint   测试点
     */
    public void newBuildProject(String projectName, TestPoint testPoint) {
        Project project = new Project();
        project.setProjectName(projectName);
        project.setMaxBuildSerialNum(0);
        List<TestPoint> testPointList = project.getTestPointList();
        testPointList.add(testPoint);

        SharedPreferences.Editor editor = preferences.edit();
        String projectInfo = preferences.getString(SHARED_KEY_PROJECT_INFO, "");
        JSONArray jsonArray = new JSONArray();
        //该sp下无任何项目，就创建该项目
        if (TextUtils.isEmpty(projectInfo)) {
            jsonArray.add(project);
        } else {
            //该项目存在
            List<Project> projects = JSON.parseArray(projectInfo, Project.class);
            if (projects == null) {
                //理论上不可能存在
                return;
            }
            for (Project project1 : projects) {
                String projectName1 = project1.getProjectName();
                if (!TextUtils.isEmpty(projectName1) && projectName1.equals(projectName)) {
                    List<TestPoint> testPointList1 = project.getTestPointList();
                    Iterator<TestPoint> iterator = testPointList1.iterator();
                    //遍历查看有相同构建序号的移除
                    while (iterator.hasNext()) {
                        TestPoint point = iterator.next();
                        if (point != null) {
                            if (point.getBuildSerialNum() == testPoint.getBuildSerialNum()) {
                                iterator.remove();
                            }
                        }
                    }
                    //添加新的构建测试点
                    testPointList.add(testPoint);
                    projects.add(project);
                    jsonArray.addAll(projects);
                } else {
                    projects.add(project);
                    jsonArray.addAll(projects);

                }
            }


        }
        editor.putString(SHARED_KEY_PROJECT_INFO, JSON.toJSONString(jsonArray));
        editor.apply();
    }

    /**
     * 对于已经存在的项目,插入一个测试点
     *
     * @param projectName 项目名称
     * @param testPoint   测试点
     */
    public void insertTestPoint(String projectName, TestPoint testPoint) {
        SharedPreferences.Editor editor = preferences.edit();
        String projectInfo = preferences.getString(SHARED_KEY_PROJECT_INFO, "");
        if (TextUtils.isEmpty(projectInfo)) {
            //理论上不可能存在
            return;
        }
        List<Project> projects = JSON.parseArray(projectInfo, Project.class);
        if (projects == null) {
            //理论上不可能存在
            return;
        }
        for (Project project : projects) {
            String projectName1 = project.getProjectName();
            if (!TextUtils.isEmpty(projectName1) && projectName1.equals(projectName)) {
                List<TestPoint> testPointList = project.getTestPointList();
                Iterator<TestPoint> iterator = testPointList.iterator();
                //遍历查看有相同构建序号的移除
                while (iterator.hasNext()) {
                    TestPoint point = iterator.next();
                    if (point != null) {
                        if (point.getBuildSerialNum() == testPoint.getBuildSerialNum()) {
                            iterator.remove();
                        }
                    }
                }
                //添加新的构建测试点
                testPointList.add(testPoint);
            }
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(projects);
        editor.putString(SHARED_KEY_PROJECT_INFO, JSON.toJSONString(jsonArray));
        editor.apply();
    }

    /**
     * 获取所有项目的项目名称
     *
     * @return 所有项目的项目名称
     */
    public List<String> getAllProjectNames() {
        List<String> projectNames = new ArrayList<>();
        String projectInfo = preferences.getString(SHARED_KEY_PROJECT_INFO, "");
        if (TextUtils.isEmpty(projectInfo)) {
            return null;
        }
        List<Project> projects = JSON.parseArray(projectInfo, Project.class);
        if (projects == null) {
            //理论上不可能存在
            return null;
        }
        for (Project project : projects) {
            if (project != null) {
                if (project.getProjectName() != null) {
                    projectNames.add(project.getProjectName());
                }
            }
        }
        return projectNames;
    }

    /**
     * 判断某个工程名称是否已经存在
     *
     * @param projectName 工程名称
     * @return 某个工程名称是否已经存在
     */
    public boolean isProjectNameExit(String projectName) {
        List<String> allProjectNames = getAllProjectNames();
        if (allProjectNames == null) {
            return false;
        }
        for (String projectNameT : allProjectNames) {
            if (!TextUtils.isEmpty(projectNameT) && TextUtils.equals(projectNameT, projectName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取某个工程的最大构建序列号
     *
     * @param projectName 工程名称
     * @return 最大构建序列号
     */
    public int getMaxBuildSerialNum(String projectName) {
        String projectInfo = preferences.getString(SHARED_KEY_PROJECT_INFO, "");
        if (TextUtils.isEmpty(projectInfo)) {
            return 0;
        }
        List<Project> projects = JSON.parseArray(projectInfo, Project.class);
        if (projects == null) {
            //理论上不可能存在
            return 0;
        }
        for (Project project : projects) {
            String projectName1 = project.getProjectName();
            if (TextUtils.isEmpty(projectName1) && projectName1.equals(projectName)) {
                return project.getMaxBuildSerialNum();
            }
        }
        return 0;
    }
}
