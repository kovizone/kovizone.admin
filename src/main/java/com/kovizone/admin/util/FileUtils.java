package com.kovizone.admin.util;

import java.io.*;
import java.util.Base64;

/**
 * 文件工具类
 *
 * @author KoviChen
 * @version 0.0.1 20200108 KoviChen 新建类
 */
public class FileUtils {

    /**
     * 生成文件Base64字符串
     *
     * @param filePath 文件路径和文件名
     * @return 文件Base64字符串
     */
    public static String getFileStr(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            return getFileStr(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            GeneralUtils.close(inputStream);
        }
        return "";
    }

    /**
     * 生成文件Base64字符串
     *
     * @param inputStream 输入流
     * @return 文件Base64字符串
     */
    public static String getFileStr(InputStream inputStream) {
        byte[] data = null;
        try {
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        // 加密
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * 构造文件
     *
     * @param fileStr 文件Base64字符串
     * @param path    文件存放路径（路径+文件名）
     * @return 构造结果
     */
    public static boolean generateFile(String fileStr, String path) {
        if (fileStr == null) {
            return false;
        }
        OutputStream out = null;
        try {
            byte[] b = Base64.getDecoder().decode(fileStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            GeneralUtils.close(out);
        }
    }
}
