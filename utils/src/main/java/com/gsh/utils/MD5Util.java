package com.gsh.utils;


import android.util.Log;

import java.security.MessageDigest;
import java.util.Locale;

public class MD5Util {
    private static String TAG = "MD5Util";

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            Log.e(TAG, "产生MD5的过程中异常");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String ChunyuAPPKey = "e8576bd340a1bec3ec1d2eecae44d5eb";
        int time = 1425887517;
        String userID = "1CHYU687FCE0D595BE8050A16FDB54966DF35";
        Log.d(TAG, "=============");
        String before = ChunyuAPPKey + time + userID;
        Log.d(TAG, "before: " + before);
        String after = MD5Util.MD5(before);
        Log.d(TAG, "after: " + after);
        String after16 = (String) after.subSequence(8, 24);
        Log.d(TAG, "after16: " + after16);
        Log.d(TAG, "after16: " + after16.toLowerCase(Locale.US));
    }
}