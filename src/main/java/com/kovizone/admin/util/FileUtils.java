package com.kovizone.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Base64;

/**
 * 文件工具类
 *
 * @author KoviChen
 * @version 0.0.1 20200108 KoviChen 新建类
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

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
            logger.error(e.getMessage(), e);
            return "";
        } finally {
            GeneralUtils.close(inputStream);
        }
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
            return Base64.getEncoder().encodeToString(data);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return "";
        }
        // 加密
    }

    public static void main(String[] args) {
        String path = "D://kovi//test4\\\\\test.jpg";
        System.out.println(path.lastIndexOf("/"));
        System.out.println(path.substring(0, path.lastIndexOf("\\")));
        new File(path.substring(0, path.lastIndexOf("\\"))).mkdirs();
    }

    /**
     * 构造文件
     *
     * @param file 文件字节数组
     * @param path 文件存放路径（路径+文件名）
     * @return 构造结果
     */
    public static boolean generateFile(byte[] file, String path) {
        if (file == null) {
            return false;
        }
        OutputStream out = null;
        try {
            String folderPath;
            int folderPathLastIndex = -1;
            folderPathLastIndex = path.lastIndexOf("/");

            if (folderPathLastIndex == -1) {
                folderPathLastIndex = path.lastIndexOf("\\");
            }
            File folder = new File(path.substring(0, folderPathLastIndex));
            if (!folder.exists()) {
                folder.mkdirs();
            }
            out = new FileOutputStream(path);
            out.write(file);
            out.flush();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            GeneralUtils.close(out);
        }
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
        try {
            byte[] b = Base64.getDecoder().decode(fileStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return generateFile(b, path);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
