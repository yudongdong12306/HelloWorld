package com.gsh.utils.android;


import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间转换工具类
 * Created by damon on 6/6/15.
 */
public class TimeUtils {
    private static final String TAG = "TimeUtils";

    /**
     * 时区有效的最小范围
     */
    private static final long TIME_ZONE_VALID_MIN_RANGE = -12 * 3600;

    /**
     * 时区有效的最大范围
     */
    private static final long TIME_ZONE_VALID_MAX_RANGE = 12 * 3600;

    private TimeUtils() {
    }


    /**
     * 格式化UNIX时间戳到本地化字符串， 包括年月日
     * 将给定的UNIX时间戳转换为String 比如:1952年1月12号 英文: Jan,1 1952
     *
     * @param timeSecond unix时间戳，单位s
     * @return 比如: 中文"1952年1月12号" 英文: "Jan,1 1952"
     */
    public static String getYMD(long timeSecond) {
        // 使用系统自带的日期格式化，MEDIUM 对应于"Jan 12, 1952". 中文为“1952年1月12日”
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return df.format(new Date(timeSecond * 1000L));
    }


    /**
     * @return 获取今天的unix
     */
    public static long getTodayUnix() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        return mCalendar.getTimeInMillis() / 1000L;
    }

    /**
     * 将unix转换为指定格式的字符串
     *
     * @param timeInSecond unix时间戳
     * @param dateFormat   格式化
     * @return 格式化后的时间字符串
     */
    public static String getTime(long timeInSecond, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInSecond * 1000L));
    }

    /**
     * 将给定的unix,转换为默认格式的字符串
     *
     * @param timeInSecond unix时间戳
     * @return 格式化后的时间字符串
     */
    public static String getTime(long timeInSecond) {
        return getTime(timeInSecond, new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()));
    }


    /**
     * 获取当前时间的unix(秒)
     *
     * @return unix时间戳
     */
    public static long getCurrentTimeInSecond() {
        return System.currentTimeMillis() / 1000L;
    }


    /**
     * 获取格式化后的当前时间 比如: 2015-06-06
     *
     * @return 默认格式化的字符串
     */
    public static String getCurrentTime() {
//        return getTime(getCurrentTimeInSecond());
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    /**
     * @return 获取今天的00:00的unix时间戳
     */
    public static long getTodayTimeAt0000() {
        return getTimeAt0000(getCurrentTimeInSecond());
    }

    /**
     * 将给定的unix时间戳转换为0点0分0秒的unix时间戳
     *
     * @param timeSecond unix时间戳
     * @return unix时间戳
     */
    public static long getTimeAt0000(long timeSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeSecond * 1000L);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return (calendar.getTimeInMillis() / 1000L);
    }

    /**
     * 获取给定unix时间戳的23:59:59秒的unix时间戳
     *
     * @param timeSecond unix时间戳
     * @return unix时间戳
     */
    public static long getTimeAt2359(long timeSecond) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(timeSecond * 1000L);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
//        calendar.set(Calendar.MILLISECOND, 999);
        return (calendar.getTimeInMillis() / 1000L);
    }

    /**
     * 获取给定的unix时间戳的第二天的00:00的unix时间戳
     *
     * @param timeSecond unix时间戳
     * @return unix时间戳
     */
    public static long getNextDayAt0000(long timeSecond) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(timeSecond * 1000L);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);

        return calendar.getTimeInMillis() / 1000L;
    }

    /**
     * 获取给定unix时间戳的前几天或后几天的00:00的时间 (day>0表示后几天,day<0表示前几天)
     *
     * @param timeSecond unix时间戳
     * @param day        timeSecond的前几天或后几天
     * @return unix时间戳
     */
    public static int getSomeDayAt0000(long timeSecond, int day) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(timeSecond * 1000L);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, day);
        return (int) (calendar.getTimeInMillis() / 1000L);
    }

    /**
     * 将给定的UNIX时间戳转换为String 比如: 比如:1999年3月25号 下午 06:31 英文: Mar, 1999 11:04 PM
     *
     * @param timeSecond unix时间戳
     * @return 中文"1999年3月25号 下午 06:31",英文"Mar, 1999 11:04 PM"
     */
    public static String getTimeFormat0(int timeSecond) {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        return df.format(new Date(timeSecond));
    }


    /**
     * 将给定的UNIX时间戳转换为String 比如: 比如:3月25号8:00 英文: Mar,3 8:00
     */
    public static String getTimeFormat4(Context context, long timeSecond) {
        SimpleDateFormat format;
        if (LocaleUtils.isChinese(context)) {
            format = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());
        } else {
            format = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
        }
        return format.format(new Date(timeSecond * 1000L));
    }

    /**
     * 将给定的UNIX时间戳转换为String 比如: 比如:3月25号 英文: Mar,3
     */
    public static String getTimeFormat5(Context context, long timeSecond) {
        SimpleDateFormat format;
        if (LocaleUtils.isChinese(context)) {
            format = new SimpleDateFormat("MMMdd日", Locale.getDefault());
        } else {
            format = new SimpleDateFormat("MMM dd", Locale.getDefault());
        }
        return format.format(new Date(timeSecond * 1000L));
    }

    /**
     * 将给定的unix时间戳转换为 上午或下午的时间, 比如: 英文 11:04 PM  中文: 下午 06:31
     *
     * @param context    context
     * @param timeSecond unix时间戳
     * @return 格式化后的字符串
     */
    public static String getTimeFormat2(Context context, long timeSecond) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date(timeSecond * 1000L));
        int amOrPm = mCalendar.get(Calendar.AM_PM);
        // int hour = mCalendar.get(Calendar.HOUR);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 12) {
            hour -= 12;
        }
        int minute = mCalendar.get(Calendar.MINUTE);
        String result;
        DecimalFormat formatter = new DecimalFormat();
        formatter.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        formatter.applyPattern("00");
        String hourS = formatter.format(hour);
        String minuteS = formatter.format(minute);
        if (LocaleUtils.isChinese(context)) {

            String temp = amOrPm == Calendar.AM ? "上午" : "下午";
            result = temp + "  " + hourS + ":" + minuteS;
        } else {
            String temp = amOrPm == Calendar.AM ? "AM" : "PM";
            result = hourS + ":" + minuteS + "  " + temp;
        }
        return result;
    }


    /**
     * 求两个unix相差的天数(相比较的两个unix对准到00:00),
     *
     * @param unix0 对准到00:00
     * @param unix1 对准到00:00
     * @return -1:数据异常,否则:返回2个时间相隔的天数
     */
    public static int getDaysInterval(long unix0, long unix1) {
        Log.d(TAG, "求两个unix相差的天数-->>unix0-->>" + unix0 + ", 格式化后-->>" + getTime(unix0) + ", unix1-->>" + unix1 + ", 格式化后-->>" + getTime(unix1));
        Calendar c = Calendar.getInstance();
        long small = unix0;
        long big = unix1;
        if (unix0 > big) {
            small = unix1;
            big = unix0;
        }
        small = getTimeAt0000(small);
        big = getTimeAt0000(big);
        c.setTimeInMillis(small * 1000L);
        int delta = -1;
        int max = 365 * 100;//防止死循环,做的最大循环数
        for (int i = 0; i < max; i++) {
//            Log.d(TAG,"求两个unix相差的天数-->>test "+i);
            if (small == big) {
                delta = i;
                break;
            }
            c.add(Calendar.DATE, 1);
            small = c.getTimeInMillis() / 1000L;
        }

        return delta;
    }


    /**
     * 获取本地的时区
     *
     * @return 时区的时间戳表示，单位s
     */
    public static long getTimeZone() {
        // 2、设置本地的时区偏移,东8去偏移 28800
//        long time = TimeZone.getDefault().getRawOffset() / 1000; // 获取本地时间距离格林尼治时间的偏移,
        long time = TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000L;
//        Log.d(TAG, "getTimeZone: 获取到的本地时区为: " + time);
        //这种写法,消除夏令时的影响
        return time;
    }

    /**
     * 判断时区是否正确
     *
     * @param timezone 时区
     * @return 正确true
     */
    public static boolean isAvailableTimeZone(int timezone) {
        // 如果时区范围异常, 填入默认时区
        if ((timezone > TIME_ZONE_VALID_MAX_RANGE) ||
                (timezone < TIME_ZONE_VALID_MIN_RANGE)) {
            return false;
        }
        return true;
    }

    /**
     * 获取睡眠的开始时间
     */
    public static long getSleepStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1991);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // 开始时间1991年1月1号18点整
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获取睡眠的结束时间
     */
    public static long getSleepEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1991);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        // 结束时间1991年1月2号17点59分59秒
        return calendar.getTimeInMillis() / 1000;
    }

}
