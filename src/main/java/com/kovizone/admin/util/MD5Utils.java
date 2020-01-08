package com.kovizone.admin.util;

import java.io.UnsupportedEncodingException;

import com.kovizone.admin.constant.CharacterConstant;
import org.springframework.util.DigestUtils;

/**
 * MD5加密工具
 * 
 * @author Kovi(kovichen@163.com)
 * @version 0.0.1 20191113 KoviChen 新增encode(byte[] bytes)
 * @version 0.0.1 20191113 KoviChen 新增encode(String arg)
 */
public class MD5Utils {

	private static final String CHARSET_NAME = CharacterConstant.CHARSET_NAME;

	public static String encode(byte[] bytes) {
		return DigestUtils.md5DigestAsHex(bytes).toUpperCase();
	}

	public static String encode(String arg) {
		try {
			return encode(arg.getBytes(CHARSET_NAME));
		} catch (UnsupportedEncodingException e) {
			return encode(arg.getBytes());
		}
	}

}
