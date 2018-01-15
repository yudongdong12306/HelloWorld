package com.gsh.utils.android;

import android.content.Context;

import java.util.Locale;

/**
 * 本地化相关的工具方法
 * Created by jun on 2017/1/16.
 */
public class LocaleUtils {

    /**
     * 注意区分的是语言， 简体中文。 比较语言和国家代码. 和系统行为保持一致"values-zh-rCN"
     *
     * @return 如果是简体中文: true, 其他返回false
     */
    public static boolean isChinese(Context context) {
        // 这个仅仅是语言的比较， 中文为"zh", 英文为"en"
//        return (RaiingApplication.context.getResources().getConfiguration().locale.getLanguage().equals(
//                Locale.SIMPLIFIED_CHINESE.getLanguage()));
        // 比较应该包含language和country。SIMPLIFIED_CHINESE包含两个部分"zh_CN"
        // 如果仅比较语言， 就会造成台湾和中国大陆语言一样， 都是"zh", 但是台湾对应的Locale为"TRADITIONAL_CHINESE"->"zh_TW"
        // 本地化判断是zh_CN, 如果此处逻辑判断和本地化不一致， 就会造成界面显示不一致， 比如台湾显示的是英文界面但是却有中文的内容。比如好孕墙模块

        Locale locale = context.getResources().getConfiguration().locale;
        Locale simplifiedChinese = Locale.SIMPLIFIED_CHINESE;
        String localeCode = locale.getCountry();
        String simplifiedChineseCode = simplifiedChinese.getCountry();
        if (!localeCode.contains(simplifiedChineseCode)) {
            // 如果不相等， 直接返回
            return false;
        }
        String localLanguage = locale.getLanguage();
        String simplifiedChineseLanguage = simplifiedChinese.getLanguage();
        if (!localLanguage.contains(simplifiedChineseLanguage)) {
            // 如果不相等， 直接返回
            return false;
        }
//        Log.d("isChinese", "isChinese1: " + locale.toString());
//        Log.d("isChinese", "isChinese2: " + simplifiedChinese.toString());
        // nexus6,Android7.0
        // isChinese: isChinese1: zh_CN_#Hans
        // isChinese: isChinese2: zh_CN

        return true;
    }


    /**
     * 判断地区是否为中国， 比较国家代码是否一致"CN"
     *
     * @return true中国  false其他
     */
    public static boolean isChina(Context context) {
        return context.getResources().getConfiguration().locale.getCountry().equals(
                Locale.CHINA.getCountry());
    }

    /**
     * 获取系统中设置的语言
     *
     * @return 比如"zh",
     */
    public static String getSystemLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    /**
     * 获取系统中设置的区域
     *
     * @return 比如中国"CN",
     */
    public static String getSystemCode(Context context) {
        return context.getResources().getConfiguration().locale.getCountry();
    }

    /**
     * 根据地区获取自定义的语言标识， 中文"cn", 其他"en"
     *
     * @return 获取自定义的语言标识， 中文"cn",其他"en"
     */
    public static String getLanguage(Context context) {
        if (LocaleUtils.isChina(context)) {
            return "cn";
        } else {
            return "en";
        }
    }

}
