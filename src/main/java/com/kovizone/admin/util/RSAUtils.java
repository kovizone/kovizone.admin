package com.kovizone.admin.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RSAUtils {
    protected static final Logger logger = LoggerFactory.getLogger(RSAUtils.class);
    private static final String KEY_RSA_TYPE = "RSA";
    /**
     * JDK方式RSA加密最大只有1024位
     */
    private static final int KEY_SIZE = 1024;
    private static final int ENCODE_PART_SIZE = KEY_SIZE / 8;
    public static final String PUBLIC_KEY_NAME = "public";
    public static final String PRIVATE_KEY_NAME = "private";

    /**
     * * 加密,key可以是公钥，也可以是私钥 RSA加密明文最长117位，需要分段加密* *
     */
    public byte[] encrypt(String message, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] me = message.getBytes();
        byte[] doFinal = null;
        byte[] doFinal2 = null;
        for (int i = 0; i < me.length; i += 117) {
            if (i + 117 > me.length) {
                doFinal2 = cipher.doFinal(me, i, me.length - i);
                doFinal = doFinal2;
            } else {
                doFinal2 = cipher.doFinal(me, i, 117);
                if (doFinal == null) {
                    doFinal = doFinal2;
                } else {
                    doFinal = join(doFinal, doFinal2);
                }
            }
        }
        return doFinal;
    }

    /**
     * 连接两个byte数组，之后返回一个新的连接好的byte数组 @param a1 @param a2 @return 一个新的连接好的byte数组
     */
    static public byte[] join(byte[] a1, byte[] a2) {
        byte[] result = new byte[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    /**
     * * 从文件读取object
     */
    public Object readFromFile(String fileName) throws Exception {
        ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName));
        Object obj = input.readObject();
        input.close();
        return obj;
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte value : b) {
            sb.append(HEXCHAR[(value & 0xf0) >>> 4]);
            sb.append(HEXCHAR[value & 0x0f]);
        }
        return sb.toString();
    }

    public static byte[] toBytes(String s) {
        byte[] bytes;
        bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    private static char[] HEXCHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    // 运营后台、管理后台、商户后台js密码加密传输用到 createRSAKeys()、encode、decode

    /**
     * 创建公钥秘钥
     */
    public static Map<String, String> createRSAKeys() {
        // 里面存放公私秘钥的Base64位加密
        Map<String, String> keyPairMap = new HashMap<>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_RSA_TYPE);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 获取公钥秘钥
            String publicKeyValue = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
            String privateKeyValue = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());

            // 存入公钥秘钥，以便以后获取
            keyPairMap.put(PUBLIC_KEY_NAME, publicKeyValue);
            keyPairMap.put(PRIVATE_KEY_NAME, privateKeyValue);
        } catch (NoSuchAlgorithmException e) {
            logger.error("当前JDK版本没找到RSA加密算法！");
            logger.error(e.getMessage(), e);
        }
        return keyPairMap;
    }

    /**
     * 公钥加密 描述： 1字节 = 8位； 最大加密长度如 1024位私钥时，最大加密长度为 128-11 = 117字节，不管多长数据，加密出来都是 128
     * 字节长度。
     */
    public static String encode(String sourceStr, String publicKeyBase64Str) {
        byte[] publicBytes = Base64.decodeBase64(publicKeyBase64Str);
        // 公钥加密
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicBytes);
        List<byte[]> alreadyEncodeListData = new LinkedList<>();

        int maxEncodeSize = ENCODE_PART_SIZE - 11;
        String encodeBase64Result = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(KEY_RSA_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] sourceBytes = sourceStr.getBytes(StandardCharsets.UTF_8);
            int sourceLen = sourceBytes.length;
            for (int i = 0; i < sourceLen; i += maxEncodeSize) {
                int curPosition = sourceLen - i;
                int tempLen = curPosition;
                if (curPosition > maxEncodeSize) {
                    tempLen = maxEncodeSize;
                }
                // 待加密分段数据
                byte[] tempBytes = new byte[tempLen];
                System.arraycopy(sourceBytes, i, tempBytes, 0, tempLen);
                byte[] tempAlreadyEncodeData = cipher.doFinal(tempBytes);
                alreadyEncodeListData.add(tempAlreadyEncodeData);
            }
            // 加密次数
            int partLen = alreadyEncodeListData.size();

            int allEncodeLen = partLen * ENCODE_PART_SIZE;
            // 存放所有RSA分段加密数据
            byte[] encodeData = new byte[allEncodeLen];
            for (int i = 0; i < partLen; i++) {
                byte[] tempByteList = alreadyEncodeListData.get(i);
                System.arraycopy(tempByteList, 0, encodeData, i * ENCODE_PART_SIZE, ENCODE_PART_SIZE);
            }
            encodeBase64Result = Base64.encodeBase64String(encodeData);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return encodeBase64Result;
    }

    /**
     * 私钥解密
     */
    public static String decode(String sourceBase64RSA, String privateKeyBase64Str) {
        byte[] privateBytes = Base64.decodeBase64(privateKeyBase64Str);
        byte[] encodeSource = Base64.decodeBase64(sourceBase64RSA);
        int encodePartLen = encodeSource.length / ENCODE_PART_SIZE;
        // 所有解密数据
        List<byte[]> decodeListData = new LinkedList<>();
        String decodeStrResult = null;
        // 私钥解密
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(KEY_RSA_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 初始化所有被解密数据长度
            int allDecodeByteLen = 0;
            for (int i = 0; i < encodePartLen; i++) {
                byte[] tempEncodedData = new byte[ENCODE_PART_SIZE];
                System.arraycopy(encodeSource, i * ENCODE_PART_SIZE, tempEncodedData, 0, ENCODE_PART_SIZE);
                byte[] decodePartData = cipher.doFinal(tempEncodedData);
                decodeListData.add(decodePartData);
                allDecodeByteLen += decodePartData.length;
            }
            byte[] decodeResultBytes = new byte[allDecodeByteLen];
            for (int i = 0, curPosition = 0; i < encodePartLen; i++) {
                byte[] tempSorceBytes = decodeListData.get(i);
                int tempSourceBytesLen = tempSorceBytes.length;
                System.arraycopy(tempSorceBytes, 0, decodeResultBytes, curPosition, tempSourceBytesLen);
                curPosition += tempSourceBytesLen;
            }
            decodeStrResult = new String(decodeResultBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return decodeStrResult;
    }

    public static void main(String[] args) {
        Map<String, String> map = RSAUtils.createRSAKeys();
        System.out.println("publicKey= " + map.get("public"));
        System.out.println("privateKey= " + map.get("private"));
    }
}
