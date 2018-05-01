package com.detect.detect.shared_preferences;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.ArraySet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.detect.detect.db.FileConstant;
import com.detect.detect.global.GlobalApplication;
import com.detect.detect.utils.JsonUtils;
import com.detect.detect.utils.MD5Utils;

import java.io.File;
import java.lang.reflect.Field;
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
    /**
     * debug 环境下允许修改 sp文件的路径
     */
    public static final boolean isDebug = true;
    /**
     * 修改以后的sp文件的路径 MyApplication.getContext().getExternalFilesDir(null).getAbsolutePath()=/sdcard/Android/%package_name%/file
     */
    public static final String FILE_PATH = FileConstant.DB_PATH;

    private ProjectNameUUIDSP() {
        preferences = getSharedPreferences(SHARED_NAME_PROJECT_NAME_MD5);
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

//    public void setProjectInfo(String projectName, ProjectInfo projectInfo) {
//        if (projectInfo == null) {
//            return;
//        }
//        if (TextUtils.isEmpty(projectName)) {
//            return;
//        }
//
//
//        //检查是否已经存在
////        List<String> allProjectNames = getAllProjectNames();
////        if (allProjectNames != null) {
////            for (String projectN : allProjectNames) {
////                if (TextUtils.equals(projectN, projectName)) {
////
////                    String jsonString = JSON.toJSONString(projectInfo);
////                    SharedPreferences.Editor editor = preferences.edit();
////
////                    Set<String> projectInfos = preferences.getStringSet("projectInfoList", null);
//////                    Set<String> projectInfoList;
//////                    if (projectInfos == null) {
//////                        projectInfoList = new HashSet<>();
//////                    } else {
//////                        projectInfoList = new HashSet<>(projectInfos);
//////                    }
//////                    projectInfoList.add(jsonString);
//////                    editor.putStringSet("projectInfoList", projectInfoList);
//////                    editor.apply();
////                    if (projectInfos!=null) {
////                        for (String info : projectInfos) {
////                            ProjectInfo parseObject = JSON.parseObject(info, ProjectInfo.class);
////                            if (parseObject!=null) {
////                                if (TextUtils.equals(parseObject.getProjectName(),projectName)) {
////
////                                }
////
////                            }
////                        }
////                    }
////
////
////
////                    return;
////                }
////            }
////        }
//        String uuid = "project" + UUID.randomUUID().toString().replaceAll("-", "");
//        projectInfo.setUuid(uuid);
////        String jsonString = JSON.toJSONString(projectInfo);
//        SharedPreferences.Editor editor = preferences.edit();
//
//
//        String projectInfoList1 = preferences.getString("projectInfoList", "");
////        List<ProjectInfo> projectInfos1 = JSON.parseArray(projectInfoList1, ProjectInfo.class);
//        JSONArray jsonArray = JSON.parseArray(projectInfoList1);
//        if (jsonArray == null) {
//            jsonArray = new JSONArray();
//        }
//        jsonArray.add(projectInfo);
//        editor.putString("projectInfoList", jsonArray.toJSONString());
//        editor.apply();
//
//
////
////        Set<String> projectInfos = preferences.getStringSet("projectInfoList", null);
////        Set<String> projectInfoList;
////        if (projectInfos == null) {
////            projectInfoList = new HashSet<>();
////        } else {
////            projectInfoList = new HashSet<>(projectInfos);
////        }
////        projectInfoList.add(jsonString);
////        editor.putStringSet("projectInfoList", projectInfoList);
////        editor.apply();
//    }


    public List<ProjectInfo> getAllProjectInfos() {
        String projectInfos = preferences.getString("projectInfoList", null);
        if (TextUtils.isEmpty(projectInfos)) {
            return null;
        }
        return JSON.parseArray(projectInfos, ProjectInfo.class);
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

    public boolean checkIsExist(String projectName) {
        if (TextUtils.isEmpty(projectName)) {
            return false;
        }
        String projectInfoList = preferences.getString("projectInfoList", "");
        if (!TextUtils.isEmpty(projectInfoList)) {
            List<ProjectInfo> projectInfos = JSON.parseArray(projectInfoList, ProjectInfo.class);
            if (projectInfos != null && projectInfos.size() > 0) {
                for (ProjectInfo info : projectInfos) {
                    if (info != null) {
                        if (TextUtils.equals(info.getProjectName(), projectName)) {
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }

//    @Deprecated
//    public List<String> getAllProjectNames() {
//        List<ProjectInfo> allProjectInfos = getAllProjectInfos();
//        if (allProjectInfos == null) {
//            return null;
//        }
//        ArrayList<String> nameList = new ArrayList<>();
//        for (ProjectInfo projectInfo : allProjectInfos) {
//            if (projectInfo == null) {
//                continue;
//            }
//            String projectName = projectInfo.getProjectName();
//            if (!TextUtils.isEmpty(projectName)) {
//                nameList.add(projectName);
//            }
//        }
//        return nameList;
//    }

    /**
     * @param fileName
     * @return isDebug = 返回修改路径(路径不存在会自动创建)以后的 SharedPreferences :%FILE_PATH%/%fileName%.xml<br/>
     * !isDebug = 返回默认路径下的 SharedPreferences : /data/data/%package_name%/shared_prefs/%fileName%.xml
     */
    private SharedPreferences getSharedPreferences(String fileName) {
        if (isDebug) {
            try {
                // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
                Field field = ContextWrapper.class.getDeclaredField("mBase");
                field.setAccessible(true);
                // 获取mBase变量
                Object obj = field.get(GlobalApplication.getContext());
                // 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
                field = obj.getClass().getDeclaredField("mPreferencesDir");
                field.setAccessible(true);
                // 创建自定义路径
                File file = new File(FILE_PATH);
                // 修改mPreferencesDir变量的值
                field.set(obj, file);
                // 返回修改路径以后的 SharedPreferences :%FILE_PATH%/%fileName%.xml
                return GlobalApplication.getContext().getSharedPreferences(fileName, Activity.MODE_PRIVATE);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 返回默认路径下的 SharedPreferences : /data/data/%package_name%/shared_prefs/%fileName%.xml
        return GlobalApplication.getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }


    //    public static final String FILE_PATH = GlobalApplication.getContext().getExternalFilesDir(null).getAbsolutePath();
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
    public void setProjectInfo(String projectName, ProjectInfo projectInfo) {
        if (TextUtils.isEmpty(projectName)) {
            return;
        }
        if (projectInfo == null) {
            return;
        }
        boolean isExist = false;
        List<ProjectInfo> projectInfos = null;
        String projectInfoList = preferences.getString("projectInfoList", "");
        if (!TextUtils.isEmpty(projectInfoList)) {
            projectInfos = JSON.parseArray(projectInfoList, ProjectInfo.class);
            if (projectInfos != null && projectInfos.size() > 0) {
                for (ProjectInfo info : projectInfos) {
                    if (info != null) {
                        if (TextUtils.equals(info.getProjectName(), projectName)) {
                            info.setConstructionOrganization(projectInfo.getConstructionOrganization());
                            info.setDetectPerson(projectInfo.getDetectPerson());
                            info.setFillerType(projectInfo.getFillerType());
                            info.setInstrumentNumber(projectInfo.getInstrumentNumber());
                            isExist = true;
                        }

                    }
                }
            }
        }
        SharedPreferences.Editor editor = preferences.edit();
        if (isExist) {
            editor.putString("projectInfoList", JSON.toJSONString(projectInfos));
            editor.apply();
        } else {
            String uuid = "project" + UUID.randomUUID().toString().replaceAll("-", "");
            projectInfo.setUuid(uuid);
            String projectInfoList1 = preferences.getString("projectInfoList", "");
            JSONArray jsonArray = JSON.parseArray(projectInfoList1);
            if (jsonArray == null) {
                jsonArray = new JSONArray();
            }
            jsonArray.add(projectInfo);
            editor.putString("projectInfoList", jsonArray.toJSONString());
            editor.apply();
        }
    }
}
