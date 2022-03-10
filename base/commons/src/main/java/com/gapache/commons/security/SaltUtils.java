package com.gapache.commons.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author HuSen
 * create on 2020/1/8 14:35
 */
public class SaltUtils {

    private static final String ALGORITHM = "SHA1PRNG";

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        // Always use a SecureRandom generator
        SecureRandom secureRandom = SecureRandom.getInstance(ALGORITHM);
        // Create array for salt
        byte[] salt = new byte[16];
        // Get a random salt
        secureRandom.nextBytes(salt);
        // return salt
        return salt;
    }
}
