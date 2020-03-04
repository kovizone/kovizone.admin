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

    /**
     * 随机值因子
     */
    private static final char[] DEFAULE_RANDOM_FACTOR = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private static final int RANDOM_NUMBER_LENGTH = 10;

    /**
     * 生成固定位数的随机值
     *
     * @param bit 随机值的位数
     * @return 随机值
     */
    public static String random(Integer bit) {
        return random(bit, DEFAULE_RANDOM_FACTOR.length);
    }

    public static String random(Integer bit, int randomLength) {
        return random(DEFAULE_RANDOM_FACTOR, bit, randomLength);
    }

    public static String random(char[] randomFactor, Integer bit) {
        return random(randomFactor, bit, randomFactor.length);
    }

    public static String random(char[] randomFactor, Integer bit, int randomLength) {
        char[] randomCharArray = new char[bit];
        Random random = new Random();
        for (int i = 0; i < bit; i++) {
            randomCharArray[i] = randomFactor[random.nextInt(randomLength)];
        }
        return String.valueOf(randomCharArray);
    }

    /**
     * 生成固定位数的随机数
     *
     * @param bit 随机值的位数
     * @return 随机数
     */
    public static String randomNumber(Integer bit) {
        return random(bit, RANDOM_NUMBER_LENGTH);
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
        if (length >= 20) {
            return arg.substring(0, 5) + buildHide(10) + arg.substring(length - 5);
        }
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

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
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
