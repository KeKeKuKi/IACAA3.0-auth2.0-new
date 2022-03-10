package com.gapache.commons.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author HuSen
 * create on 2020/1/8 14:45
 */
public class HashUtils {

    public static final String MD5 = "MD5";
    private static final String SHA_1 = "SHA-1";
    private static final String SHA_256 = "SHA-256";
    private static final String SHA_384 = "SHA-384";
    private static final String SHA_512 = "SHA-512";

    /**
     * 使用散列算法将消息变为摘要
     *
     * @param raw       消息
     * @param algorithm 算法
     * @return 摘要
     */
    public static String encrypt(byte[] raw, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(raw);
            byte[] bytes = md.digest();
            return HexUtil.byteArrayToHexString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用散列算法将消息变为摘要（加盐）
     *
     * @param raw       消息
     * @param salt      盐值
     * @param algorithm 算法
     * @return 摘要
     * @throws NoSuchAlgorithmException 没有这个加密算法
     */
    public static String encrypt(byte[] raw, byte[] salt, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(salt);
        byte[] bytes = md.digest(raw);
        return HexUtil.byteArrayToHexString(bytes);
    }
}
