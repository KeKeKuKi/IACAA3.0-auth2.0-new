package com.gapache.commons.security;

/**
 * @author HuSen
 */
public class HexUtil {

    /**
     * 字节数组转换成十六进制字符串
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder hs = new StringBuilder();
        String temp;
        for (byte b : bytes) {
            temp = Integer.toHexString(b & 0XFF);
            if (temp.length() == 1) {
                hs.append("0");
            }
            hs.append(temp);
        }
        return hs.toString();
    }

    /**
     * 十六进制字符串转为字节数组
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        final int two = 2;
        if (hexString.length() % two != 0) {
            throw new IllegalArgumentException();
        }
        char[] chars = hexString.toCharArray();
        byte[] bytes = new byte[hexString.length() / two];
        for (int i = 0, j = 0; i < hexString.length(); i++, j++) {
            String swap = "" + chars[i++] + chars[i];
            int byteInt = Integer.parseInt(swap, 16) & 0XFF;
            bytes[j] = new Integer(byteInt).byteValue();
        }
        return bytes;
    }
}