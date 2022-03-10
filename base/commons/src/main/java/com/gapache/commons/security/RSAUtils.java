package com.gapache.commons.security;

import com.gapache.commons.utils.IStringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/8 10:10
 */
public class RSAUtils {

    /**
     * 指定加密算法为RSA
     */
    private static final String ALGORITHM = "RSA";

    /**
     * 密钥长度，用来初始化
     */
    private static final int KEY_SIZE = 1024;

    public static final String PUBLIC_KEY_FILE = "PublicKey";

    public static final String PRIVATE_KEY_FILE = "PrivateKey";

    /**
     * 传入随机种子，生成秘钥对并返回
     *
     * @param randomSeed 随机种子
     * @return 秘钥对
     * @throws NoSuchAlgorithmException 没有找到此算法
     */
    public static Map<String, String> genKeyPair(byte[] randomSeed) throws NoSuchAlgorithmException {
        // RSA算法要求有一个可信任的随机数源
        SecureRandom secureRandom = new SecureRandom(randomSeed);

        // 为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);

        // 利用上面的随机数据源初始化这个KeyPairGenerator对象
        keyPairGenerator.initialize(KEY_SIZE, secureRandom);

        // 生成密匙对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 得到公钥
        Key publicKey = keyPair.getPublic();

        // 得到私钥
        Key privateKey = keyPair.getPrivate();

        byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();

        String publicKeyBase64 = new BASE64Encoder().encode(publicKeyBytes);
        String privateKeyBase64 = new BASE64Encoder().encode(privateKeyBytes);
        Map<String, String> keyPairMap = new HashMap<>(2);
        keyPairMap.put(PUBLIC_KEY_FILE, publicKeyBase64);
        keyPairMap.put(PRIVATE_KEY_FILE, privateKeyBase64);
        return keyPairMap;
    }

    /**
     * 获取公钥
     *
     * @param publicKeyBase64 公钥Base64编码字符串
     * @return 公钥
     * @throws IOException              IOException
     * @throws NoSuchAlgorithmException 没有找到此算法
     * @throws InvalidKeySpecException  InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String publicKeyBase64) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = new BASE64Decoder().decodeBuffer(publicKeyBase64);
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(bytes);
        return KeyFactory.getInstance(ALGORITHM).generatePublic(encodedKeySpec);
    }

    /**
     * 获取私钥
     *
     * @param privateKeyBase64 私钥Base64编码字符串
     * @return 私钥
     * @throws IOException              IOException
     * @throws NoSuchAlgorithmException 没有找到此算法
     * @throws InvalidKeySpecException  InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String privateKeyBase64) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = new BASE64Decoder().decodeBuffer(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(keySpec);
    }

    /**
     * 使用公钥进行加密
     *
     * @param rawData   原始数据
     * @param publicKey 公钥
     * @return 加密后的数据
     * @throws NoSuchPaddingException    NoSuchPaddingException
     * @throws NoSuchAlgorithmException  NoSuchAlgorithmException
     * @throws InvalidKeyException       InvalidKeyException
     * @throws BadPaddingException       BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     */
    public static byte[] encrypt(byte[] rawData, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 加密数据, 返回加密后的密文
        return cipher.doFinal(rawData);
    }

    /**
     * 使用私钥进行加密
     *
     * @param rawData    原始数据
     * @param privateKey 私钥
     * @return 加密后的数据
     * @throws NoSuchPaddingException    NoSuchPaddingException
     * @throws NoSuchAlgorithmException  NoSuchAlgorithmException
     * @throws InvalidKeyException       InvalidKeyException
     * @throws BadPaddingException       BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     */
    public static byte[] encrypt(byte[] rawData, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        // 加密数据, 返回加密后的密文
        return cipher.doFinal(rawData);
    }

    /**
     * 使用私钥解密
     *
     * @param encodedData 加密的数据
     * @param privateKey  私钥
     * @return 解密后的数据
     * @throws NoSuchPaddingException    NoSuchPaddingException
     * @throws NoSuchAlgorithmException  NoSuchAlgorithmException
     * @throws InvalidKeyException       InvalidKeyException
     * @throws BadPaddingException       BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     */
    public static byte[] decrypt(byte[] encodedData, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化密码器（私钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 解密数据, 返回解密后的明文
        return cipher.doFinal(encodedData);
    }

    /**
     * 使用公钥解密
     *
     * @param encodedData 加密的数据
     * @param publicKey   公钥
     * @return 解密后的数据
     * @throws NoSuchPaddingException    NoSuchPaddingException
     * @throws NoSuchAlgorithmException  NoSuchAlgorithmException
     * @throws InvalidKeyException       InvalidKeyException
     * @throws BadPaddingException       BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     */
    public static byte[] decrypt(byte[] encodedData, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化密码器（私钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        // 解密数据, 返回解密后的明文
        return cipher.doFinal(encodedData);
    }

    public static String encrypt(String rawData, PrivateKey privateKey) {
        try {
            return IStringUtils.newString(RSAUtils.encrypt(IStringUtils.getBytes(rawData), privateKey));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String decrypt(String encryptData, PublicKey publicKey) {
        try {
            return IStringUtils.newString(RSAUtils.encrypt(IStringUtils.getBytes(encryptData), publicKey));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Map<String, String> map = RSAUtils.genKeyPair(IStringUtils.getBytes("jobServerToJobExecutor"));
        System.out.println(map.get("PublicKey"));
        System.out.println("============");
        System.out.println(map.get("PrivateKey"));
        System.out.println("============");
        System.out.println("============");
        map = RSAUtils.genKeyPair(IStringUtils.getBytes("jobExecutorToJobServer"));
        System.out.println(map.get("PublicKey"));
        System.out.println("============");
        System.out.println(map.get("PrivateKey"));
    }
}
