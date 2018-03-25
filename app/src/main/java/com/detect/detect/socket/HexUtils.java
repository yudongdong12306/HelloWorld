package com.detect.detect.socket;
/**
 * reference apache commons <a
 * href="http://commons.apache.org/codec/">http://commons.apache.org/codec/</a>
 *
 * @author Aub
 */
public class HexUtils {

    private static final String TAG = "HexUtils";
    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private static final char[] DIGITS_UPPER = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };


    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    private static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
     * @return 十六进制char[]
     */
    private static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l * 5];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = '0';
            out[j++] = 'x';
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
            out[j++] = ',';
        }
        return out;
    }
    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @return 十六进制String
     */
    private static String encodeHexStr(byte[] data) {
        return encodeHexStr(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
     * @return 十六进制String
     */
    private static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制String
     */
    protected static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    /**
     * 将十六进制字符数组转换为字节数组
     *
     * @param data 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    private static byte[] decodeHex(char[] data) {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * 将十六进制字符转换成一个整数
     *
     * @param ch    十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * byte格式成为16进制的字符串输出，主要用来解析接收到的数据
     *
     * @param content 需要输出的字节数据
     * @return
     */
    public static String byteToString(byte[] content) {
        if (content == null) {
//           BleLog.e(TAG, "byteToString: 传入的字节数组为null");
            return null;
        }
        String byteToStr = "";
        for (byte aContent : content) {
            byteToStr += String.format("%02X ", aContent);
        }
        return byteToStr;
    }

    /**
     * 把 byte格式化字符串输出，主要用来解析接收到的数据
     *
     * @param content 具体要转换的字节内容
     * @param offset  在字节数据中的偏移
     * @param len     字节数组需要转换的长度
     * @return 返回指定直接数组的字符串标识形式
     */
    public static String byteToAsciiString(byte[] content, int offset, int len) {
        StringBuilder byteToStr = new StringBuilder();
        for (int i = 0; i < len; i++) {
            // byteToStr += String.format("%02X ", content[offset+i]);
            // byteToStr += String.format("%d ", content[offset+i]);
            char ch = (char) content[offset + i];
            byteToStr.append(ch);
        }
        return byteToStr.toString();
    }


    /**
     * 把 byte格式化字符串输出，主要用来解析接收到的数据
     *
     * @param content 具体要转换的字节内容
     * @return 返回指定直接数组的字符串标识形式
     */
    public static String byteToAsciiString(byte[] content) {
        if (content == null) {
            return null;
        }
        int len = content.length;
        StringBuilder byteToStr = new StringBuilder();
        for (int i = 0; i < len; i++) {
            // byteToStr += String.format("%02X ", content[offset+i]);
            // byteToStr += String.format("%d ", content[offset+i]);
            char ch = (char) content[i];
            byteToStr.append(ch);
        }
        return byteToStr.toString();
    }
}
