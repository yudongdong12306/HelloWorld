package com.detect.detect.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.ArraySet;

import com.alibaba.fastjson.JSON;
import com.detect.detect.utils.JsonUtils;
import com.detect.detect.utils.MD5Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class ProjectNameUUIDSP {

    private static final String SHARED_NAME_PROJECT_NAME_MD5 = "project_name_md5";
    private static Context sContext;
    private static ProjectNameUUIDSP sSharedPrefHelper;
    private SharedPreferences preferences;

    private ProjectNameUUIDSP() {
        preferences = sContext.getSharedPreferences(SHARED_NAME_PROJECT_NAME_MD5, Context.MODE_PRIVATE);
    }

    /**
     * 初始化
     *
     * @param context 全局的context，在Application中进行初始化
     */
    public static void initialize(Context context) {
        sContext = context;
    }


    public static ProjectNameUUIDSP getInstance() {
        if ((sContext == null)) {
            throw new IllegalArgumentException(TAG + "必须先调用initialize方法初始化为全局的Context对象");
        }
        if (null == sSharedPrefHelper) {
            synchronized (ProjectNameUUIDSP.class) {
                if (null == sSharedPrefHelper) {
                    sSharedPrefHelper = new ProjectNameUUIDSP();
                }
            }
        }
        return sSharedPrefHelper;
    }

//    //双向保存
//    @Deprecated
//    public void setProjectNameMd5(String projectName) {
//        if (TextUtils.isEmpty(projectName)) {
//            return;
//        }
////        String uuidProject = getUUIDFromProjectName(projectName);
////        if (!TextUtils.isEmpty(uuidProject)) {
////            //已经保存过了
////            return;
////        }
//        SharedPreferences.Editor editor = preferences.edit();
//        String uuid = "project" + UUID.randomUUID().toString().replaceAll("-", "");
//        editor.putString(projectName, uuid);
//        editor.putString(uuid, projectName);
//        editor.apply();
//    }
    //////////////------------------------------------//

    public void setProjectInfo(String projectName, ProjectInfo projectInfo) {
        if (projectInfo == null) {
            return;
        }
        if (TextUtils.isEmpty(projectName)) {
            return;
        }
        //检查是否已经存在
        List<String> allProjectNames = getAllProjectNames();
        if (allProjectNames != null) {
            for (String projectN : allProjectNames) {
                if (TextUtils.equals(projectN, projectName)) {
                    return;
                }
            }
        }
        String uuid = "project" + UUID.randomUUID().toString().replaceAll("-", "");
        projectInfo.setUuid(uuid);
        String jsonString = JSON.toJSONString(projectInfo);
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> projectInfos = preferences.getStringSet("projectInfoList", null);
        Set<String> projectInfoList;
        if (projectInfos == null) {
            projectInfoList = new HashSet<>();
        } else {
            projectInfoList = new HashSet<>(projectInfos);
        }
        projectInfoList.add(jsonString);
        editor.putStringSet("projectInfoList", projectInfoList);
        editor.apply();
    }


    public List<ProjectInfo> getAllProjectInfos() {
        ArrayList<ProjectInfo> projectInfosList = new ArrayList<>();
        Set<String> projectInfos = preferences.getStringSet("projectInfoList", null);
        if (projectInfos == null) {
            return null;
        }
        for (String projectInfoStr : projectInfos) {
            if (TextUtils.isEmpty(projectInfoStr)) {
                continue;
            }
            ProjectInfo projectInfo = JSON.parseObject(projectInfoStr, ProjectInfo.class);
            projectInfosList.add(projectInfo);
        }
        return projectInfosList;
    }

    public ProjectInfo getProjectInfo(String projectName) {
        if (TextUtils.isEmpty(projectName)) {
            return null;
        }
        List<ProjectInfo> allProjectInfos = getAllProjectInfos();
        if (allProjectInfos == null) {
            return null;
        }
        for (ProjectInfo projectInfo : allProjectInfos) {
            if (projectInfo == null) {
                continue;
            }
            if (TextUtils.equals(projectInfo.getProjectName(), projectName)) {
                return projectInfo;
            }

        }
        return null;

    }

    @Deprecated
    public List<String> getAllProjectNames() {
        List<ProjectInfo> allProjectInfos = getAllProjectInfos();
        if (allProjectInfos == null) {
            return null;
        }
        ArrayList<String> nameList = new ArrayList<>();
        for (ProjectInfo projectInfo : allProjectInfos) {
            if (projectInfo == null) {
                continue;
            }
            String projectName = projectInfo.getProjectName();
            if (!TextUtils.isEmpty(projectName)) {
                nameList.add(projectName);
            }
        }
        return nameList;
    }

    //------------------------//
//    @Deprecated
//    public ProjectInfo getProjectInfoFromUUID(String uuid) {
//        if (TextUtils.isEmpty(uuid)) {
//            return null;
//        }
//        String string = preferences.getString("project_info" + uuid, "");
//        return JSON.parseObject(string, ProjectInfo.class);
//    }
//
//    @Deprecated
//    public String getProjectNameFromMd5(String tableName) {
//        return preferences.getString(tableName, "");
//    }
//
//    @Deprecated
//    public String getUUIDFromProjectName(String projectName) {
//        return preferences.getString(projectName, "");
//    }
}
