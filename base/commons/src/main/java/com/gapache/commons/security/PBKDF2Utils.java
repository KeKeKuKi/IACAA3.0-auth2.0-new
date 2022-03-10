package com.gapache.commons.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Java将 PBKDF2 算法实现为 PBKDF2WithHmacSHA1
 *
 * @author HuSen
 * create on 2020/1/8 15:07
 */
public class PBKDF2Utils {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    /**
     * PBKDF2加密
     *
     * @param rawText 原始明文
     * @return 加密后的密文
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException  InvalidKeySpecExceptions
     */
    public static String encrypt(String rawText) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final int iterations = 1000;
        char[] chars = rawText.toCharArray();
        byte[] salt = SaltUtils.getSalt();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 512);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return iterations + ":" + HexUtil.byteArrayToHexString(salt) + ":" + HexUtil.byteArrayToHexString(hash);
    }

    /**
     * 验证
     *
     * @param raw     原始明文
     * @param encoded 加密密文
     * @return 是否一致
     */
    public static boolean validate(String raw, String encoded) {
        try {
            String[] parts = encoded.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = HexUtil.hexStringToByteArray(parts[1]);
            byte[] hash = HexUtil.hexStringToByteArray(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(raw.toCharArray(), salt, iterations, 512);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] testHash = factory.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for (int i = 0; i < hash.length && i < testHash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
