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

    private static final String TAB = "\t";

    private static final int TAB_WIDTH = 4;

    public static void main(String[] args) {
        int maxTabSize = 7;
        System.out.println(smartTab("", maxTabSize));
        System.out.println(smartTab("1", maxTabSize));
        System.out.println(smartTab("12", maxTabSize));
        System.out.println(smartTab("123", maxTabSize));
        System.out.println(smartTab("12345", maxTabSize));
        System.out.println(smartTab("123456", maxTabSize));
        System.out.println(smartTab("1234567", maxTabSize));
        System.out.println(smartTab("12345678", maxTabSize));
        System.out.println(smartTab("123456789", maxTabSize));
        System.out.println(smartTab("1234567890", maxTabSize));
        System.out.println(smartTab("12345678901", maxTabSize));
        System.out.println(smartTab("123456789012", maxTabSize));
        System.out.println(smartTab("1234567890123", maxTabSize));
        System.out.println(smartTab("12345678901234", maxTabSize));
        System.out.println(smartTab("123456789012345", maxTabSize));
        System.out.println(smartTab("1234567890123456", maxTabSize));
        System.out.println(smartTab("12345678901234567", maxTabSize));
        System.out.println(smartTab("123456789012345678", maxTabSize));
        System.out.println(smartTab("1234567890123456789", maxTabSize));
        System.out.println(smartTab("12345678901234567890", maxTabSize));
        System.out.println(smartTab("123456789012345678901", maxTabSize));
    }

    /**
     * 自动添加tab
     *
     * @param arg        原字符串
     * @param maxTabSize tab上限
     * @return 新字符串
     */
    public static String smartTab(String arg, int maxTabSize) {
        return (arg == null ? "" : arg) +
                buildTab(maxTabSize - ((StringUtils.isEmpty(arg) ? 0 : arg.length()) / TAB_WIDTH));
    }

    /**
     * 生成指定数量的tab
     *
     * @param size 数量
     * @return tab字符串
     */
    public static String buildTab(int size) {
        return size <= 0 ? "" : (TAB + buildTab(--size));
    }

    /**
     * 判断 值不为空并且不为null值
     *
     * @param str 字符串
     * @return true:为空或是null值  false: 不为空和null
     */
    public static boolean isEmpty(String str) {
        return !(str != null && !"".equals(str.trim()));
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

    private static final String HIDE = "*";


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
        return HIDE;
    }

    /**
     * 生成*
     *
     * @param bit 位数
     * @return bit位的*
     */
    public static String buildHide(int bit) {
        return bit > 0 ? (HIDE + buildHide(--bit)) : "";
    }
}
