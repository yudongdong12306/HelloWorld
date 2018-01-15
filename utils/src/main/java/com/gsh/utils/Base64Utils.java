package com.gsh.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * Base64解码编码和同步温度数据的加码编码
 */

public class Base64Utils {
    /**
     * 将需要编码的字节数组
     *
     * @param b 需要编码的 字节数组
     * @return 编码后的String
     */
    public static String getBASE64(byte[] b) {
        String s = null;
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    /**
     * 将编码后的字符串,解码后得到byte数组.
     *
     * @param s String
     * @return 字节数组
     */
    public static byte[] getFromBASE64(String s) {
        byte[] b = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    /**
     * 先解码再解压缩得到String
     *
     * @param encodedBase64 需要解码字符串
     * @return
     * @throws IOException
     */
    public static String inflateBase64(String encodedBase64) throws IOException {
        byte[] bytes = Base64Utils.getFromBASE64(encodedBase64);
        byte[] inflatedBytes = new byte[1024];
        String result = new String();
        InputStream is = new ByteArrayInputStream(bytes);
        FilterInputStream filterStream = new GZIPInputStream(is);
        int outLength;
        do {
            outLength = filterStream.read(inflatedBytes);

            if (outLength > 0) {
                result += new String(inflatedBytes, 0, outLength);
            }
        } while (outLength > 0);

        return result;
    }

    /**
     * 先压缩,在编码
     *
     * @param raw 需要压缩的字符串
     * @return
     * @throws IOException
     */
    public static String deflateBase64(String raw) throws IOException {
        byte[] rawBytes = raw.getBytes("UTF-8");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        GZIPOutputStream filterStream = new GZIPOutputStream(os);
        filterStream.write(rawBytes);
        filterStream.finish();
        String result = Base64Utils.getBASE64(os.toByteArray());
        return result;
    }
}
