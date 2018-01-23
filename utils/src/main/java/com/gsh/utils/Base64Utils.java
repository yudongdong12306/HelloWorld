//package com.gsh.utils;
//
//import android.text.TextUtils;
//import android.util.Base64;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.FilterInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.zip.GZIPInputStream;
//import java.util.zip.GZIPOutputStream;
//
//import Decoder.BASE64Decoder;
//import Decoder.BASE64Encoder;
//
///**
// * Base64解码编码和同步温度数据的加码编码
// */
//public class Base64Utils {
//    //加密
//    public static String encode(String str) {
//        if (TextUtils.isEmpty(str)) {
//            return null;
//        }
//        String result = null;
//        try {
//            byte[] b = str.getBytes("utf-8");
//            result = Base64.encodeToString(b, Base64.DEFAULT);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    // 解密
//    public static String decode(String s) {
//        if (TextUtils.isEmpty(s)) {
//            return null;
//        }
//        String result = null;
//        try {
//            byte[] b = Base64.decode(s, Base64.DEFAULT);
//            result = new String(b, "utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//}
