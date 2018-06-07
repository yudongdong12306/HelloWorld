package com.detect.detect.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by hexiu on 2017/5/2.
 * 正则
 */
public class StringUtils {
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern.compile("^((13[0-9])|170|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    private final static Pattern nickNames = Pattern.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
    private final static Pattern nickNames1 = Pattern.compile("^[a-zA-Z0-9]");
    private final static Pattern nickNames2 = Pattern.compile("^[\u4E00-\u9FA5]");
    private final static Pattern names = Pattern.compile("^[\u4E00-\u9FA5]{2,6}$");
    private static final String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
    private static final String limitEx1 = "^[\\u4e00-\\u9fa5]*$";//zhongwen


    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime() {
        return getDataTime("yyyy-MM-dd HH:mm");
    }

    public static String getTime(String time) {
        String tempTime = time + "000";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(new Date(Long.parseLong(tempTime)));
    }

    /**
     * 毫秒值转换为mm:ss
     *
     * @param ms
     * @author kymjs
     */
    public static String timeFormat(int ms) {
        StringBuilder time = new StringBuilder();
        time.delete(0, time.length());
        ms /= 1000;
        int s = ms % 60;
        int min = ms / 60;
        if (min < 10) {
            time.append(0);
        }
        time.append(min).append(":");
        if (s < 10) {
            time.append(0);
        }
        time.append(s);
        return time.toString();
    }

    /**
     * 将字符串转位日期类型
     *
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    public static boolean isName(String name) {
        if (name == null || name.trim().length() == 0)
            return false;
        return names.matcher(name).matches();
    }

    /**
     * 根据
     * 1. "汉字区间"
     * 2. "数字区间"
     * 3. "小写字母区间"
     * 4. "大写字母区间"
     * 过滤掉非法字符的方法
     *
     * @param oldString
     * @return
     */
    public static boolean filterCharToNormal(String oldString) {
        int length = oldString.length();
        for (int i = 0; i < length; i++) {//遍历传入的String的所有字符
            char codePoint = oldString.charAt(i);
            if (//如果当前字符为常规字符,则将该字符拼入StringBuilder
                    ((codePoint >= 0x4e00) && (codePoint <= 0x9fa5)) ||//表示汉字区间
                            ((codePoint >= 0x30) && (codePoint <= 0x39)) ||//表示数字区间
                            ((codePoint >= 0x41) && (codePoint <= 0x5a)) ||//表示大写字母区间
                            ((codePoint >= 0x61) && (codePoint <= 0x7a))) {//小写字母区间
            } else {//如果当前字符为非常规字符,则忽略掉该字符
                return false;
            }
        }
        return true;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    public static boolean isLegalNickName(String nickName) {
        return filterCharToNormal(nickName);
//        if (nickName == null || nickName.trim().length() == 0 || nickName.trim().length() >32)
//            return false;
//        Pattern pattern = Pattern.compile(limitEx);
//        Matcher m = pattern.matcher(nickName);
//        return !m.find();
//        String limitEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//        return nickNames.matcher(nickName).matches();
    }

    public static int chineseNickNameLength(String nickName) {
        if (nickName == null || nickName.trim().length() == 0 || nickName.trim().length() > 12)
            return 0;
        else {
            int length = 0;
            for (int i = 0; i < nickName.length(); i++) {
                char item = nickName.charAt(i);
                String str = String.valueOf(item);
                if (nickNames2.matcher(str).matches()) {
                    length += 1;
                }
            }
            return length;
        }

    }

    public static int nickNameLength(String nickName) {
        if (nickName == null || nickName.trim().length() == 0 || nickName.trim().length() > 12)
            return 0;
        else {
            int length = 0;
            for (int i = 0; i < nickName.length(); i++) {
                char item = nickName.charAt(i);
                String str = String.valueOf(item);
                if (nickNames1.matcher(str).matches()) {
                    length += 1;
                } else if (nickNames2.matcher(str).matches()) {
                    length += 2;
                } else {
                    length += 1;
                }
            }
            return length;
        }

    }
}
