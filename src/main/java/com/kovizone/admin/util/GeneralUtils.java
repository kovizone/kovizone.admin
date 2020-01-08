package com.kovizone.admin.util;

/**
 * 通用工具类
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-16 KoviChen 新建类
 */
public class GeneralUtils {

	/**
	 * 关闭多个资源
	 * 
	 * @param autoCloseables 需要关闭的资源
	 */
	public static void close(AutoCloseable... autoCloseables) {
		for (AutoCloseable autoCloseable : autoCloseables) {
			if (autoCloseable != null) {
				try {
					autoCloseable.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
