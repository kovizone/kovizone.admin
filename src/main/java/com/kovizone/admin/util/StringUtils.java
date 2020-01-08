package com.kovizone.admin.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Random;

/**
 * 字符串工具类<BR>
 * 继承org.springframework.util.StringUtils
 *
 * @author Kovi(kovichen @ 163.com)
 * @version 0.0.1 201911133 KoviChen 新增upperFirstLatter(String)
 */
public class StringUtils extends org.springframework.util.StringUtils {

    private static final String NONCE_FACTOR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final int NONCE_LENGTH = 16;

    private static final char FIRST_UPPER_CASE = 'A';

    private static final char LAST_UPPER_CASE = 'Z';

    private static final char FIRST_LOWER_CASE = 'a';

    private static final char LAST_LOWER_CASE = 'z';

    public static final String SYMBOL_PLUS = "+";

    public static final String SYMBOL_MINUS = "-";


    /**
     * 如果参数为正数将转为负数,为正则相反
     *
     * @param integer 数值
     * @return 转换后的数值
     */
    public static Integer minusAndplusConversion(Integer integer) {
        if (isEmpty(integer)) {
            return null;
        }
        String intStr = integer + "";
        String substring = intStr.substring(0, 1);
        if (SYMBOL_MINUS.equals(substring)) {
            intStr = intStr.replace(SYMBOL_MINUS, "");
            return new Integer(intStr);
        } else {
            intStr = SYMBOL_MINUS + intStr;
            return new Integer(intStr);
        }
    }

    /**
     * 判断 值不为空并且不为null值
     *
     * @param str 字符串
     * @return true:为空或是null值  false: 不为空和null
     */
    public static boolean isEmpty(String str){
        return !(str != null && !"".equals(str.trim()));
    }

    public static void main(String[] args) {
        System.out.println(isEmpty("安抚"));
    }

    /**
     * 生成随机字符串
     *
     * @return 随机字符串
     */
    public static String createNonceStr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < NONCE_LENGTH; i++) {
            sb.append(NONCE_FACTOR.charAt(new Random().nextInt(NONCE_FACTOR.length())));
        }
        return sb.toString();
    }

    /**
     * 首字母大写
     *
     * @param arg
     * @return
     */
    public static String upperFirstLatter(String arg) {
        char[] chars = arg.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char) (chars[0] - 32);
        }
        return new String(chars);
    }

    /**
     * 解析Reader
     *
     * @param reader reader
     * @return 字符串
     */
    public static String parseReader(Reader reader) {
        if (reader == null) {
            return "";
        }
        try {
            StringBuffer sb = new StringBuffer();
            int read;
            while ((read = reader.read()) != -1) {
                sb.append((char) read);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 脱敏处理
     *
     * @param arg 原文
     * @return 脱敏文
     */
    public static String hide(String arg) {
        if (arg == null) {
            return null;
        }
        int length = arg.length();
        if (length >= 12) {
            return arg.substring(0, 4) + buildHide(length - 8) + arg.substring(length - 4);
        }
        if (length == 11) {
            return arg.substring(0, 3) + buildHide(length - 7) + arg.substring(length - 4);
        }
        if (length >= 6) {
            return arg.substring(0, 2) + buildHide(length - 4) + arg.substring(length - 2);
        }
        if (length >= 3) {
            return arg.substring(0, 1) + buildHide(length - 2) + arg.substring(length - 1);
        }
        if (length >= 2) {
            return arg.substring(0, 1) + buildHide(length - 1);
        }
        return arg;
    }

    /**
     * 生成*
     *
     * @param bit 位数
     * @return bit位的*
     */
    public static String buildHide(int bit) {
        if (bit > 0) {
            return "*" + buildHide(bit - 1);
        }
        return "";
    }
}
