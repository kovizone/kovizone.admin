package com.kovizone.admin.util;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具
 *
 * @author Kovi(kovichen @ 163.com)
 * @version 0.0.1 20191113 KoviChen 新建decrypt(String content, String decryptKey)
 */
public class AESUtils {

    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final String CHARSET_NAME = "UTF-8";

    /**
     * AES 加密操作
     *
     * @param content    待加密内容
     * @param encryptKey 密钥
     * @return 返回Base64转码后的加密数据
     * @throws Exception 各种报错
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        // 密钥进行MD5加密
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(encryptKey.getBytes(CHARSET_NAME));
        byte[] digest = md.digest();

        // 初始化为加密模式的密码器
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(digest, KEY_ALGORITHM));
        byte[] result = cipher.doFinal(content.getBytes(CHARSET_NAME));// 加密
        return Base64.getMimeEncoder().encodeToString(result);
    }

    /**
     * AES 解密操作
     *
     * @param content    待解密密文
     * @param decryptKey 密钥
     * @return 返回原文
     * @throws Exception 各种报错
     */
    public static String decrypt(String content, String decryptKey) throws Exception {
        // 密钥进行MD5加密
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(decryptKey.getBytes(CHARSET_NAME));
        byte[] digest = md.digest();

        // 初始化为解密模式的密码器
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(digest, KEY_ALGORITHM));
        byte[] result = cipher.doFinal(Base64.getMimeDecoder().decode(content));
        return new String(result, CHARSET_NAME);
    }

}