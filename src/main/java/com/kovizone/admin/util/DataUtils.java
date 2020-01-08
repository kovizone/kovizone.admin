package com.kovizone.admin.util;

import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Random;

import javax.servlet.ServletRequest;

public class DataUtils {

	/**
	 * 是否为空（空字符串）
	 *
	 * @param object 对象
	 * @return 是空或空字符串
	 */
	public static boolean isEmpty(Object object) {
		return object == null || object.toString().trim().equals("");
	}

	public static String reader2String(Reader reader) {
		try {
			BufferedReader reader2 = (BufferedReader) reader;
			StringBuffer sb = new StringBuffer();
			String str = "";
			while ((str = reader2.readLine()) != null) {
				sb.append(str).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Request转Object
	 *
	 * @param request Http请求
	 * @param clazz   转换类
	 * @return 实例化结果
	 */
	public static <T> T request2Object(ServletRequest request, Class<T> clazz) {
		T t = null;
		try {
			Class<?> clazz2 = clazz;
			t = clazz.getDeclaredConstructor().newInstance();
			for (; clazz2 != Object.class; clazz2 = clazz2.getSuperclass()) {
				Field[] fields = clazz2.getDeclaredFields();
				if (fields != null) {
					for (Field field : fields) {
						field.setAccessible(true);
						String name = field.getName();
						String value = request.getParameter(name);
						if (value == null) {
							continue;
						}
						String typeString = field.getGenericType().toString();
						if (typeString.equals("class java.lang.String")) {
							if (value.equals("")) {
								continue;
							}
							field.set(t, value);
						}
						if (typeString.equals("byte")) {
							field.set(t, Byte.parseByte(value));
						}
						if (typeString.equals("class java.lang.Byte")) {
							field.set(t, string2Byte(value));
						}
						if (typeString.equals("short")) {
							field.set(t, Short.parseShort(value));
						}
						if (typeString.equals("class java.lang.Short")) {
							field.set(t, string2Short(value));
						}
						if (typeString.equals("int")) {
							field.set(t, Integer.parseInt(value));
						}
						if (typeString.equals("class java.lang.Integer")) {
							field.set(t, string2Integer(value));
						}
						if (typeString.equals("long")) {
							field.set(t, Long.parseLong(value));
						}
						if (typeString.equals("class java.lang.Long")) {
							field.set(t, string2Long(value));
						}
						if (typeString.equals("float")) {
							field.set(t, Float.parseFloat(value));
						}
						if (typeString.equals("class java.lang.Float")) {
							field.set(t, string2Float(value));
						}
						if (typeString.equals("double")) {
							field.set(t, Double.parseDouble(value));
						}
						if (typeString.equals("class java.lang.Double")) {
							field.set(t, string2Double(value));
						}
						if (typeString.equals("class java.util.Date")) {
							field.set(t, DateUtil.parseDate(value));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public static Byte string2Byte(String arg) {
		try {
			return Byte.parseByte(arg);
		} catch (Exception e) {
			return null;
		}
	}

	public static Short string2Short(String arg) {
		try {
			return Short.parseShort(arg);
		} catch (Exception e) {
			return null;
		}
	}

	public static Integer string2Integer(String arg) {
		try {
			return Integer.parseInt(arg);
		} catch (Exception e) {
			return null;
		}
	}

	public static Long string2Long(String arg) {
		try {
			return Long.parseLong(arg);
		} catch (Exception e) {
			return null;
		}
	}

	public static Float string2Float(String arg) {
		try {
			return Float.parseFloat(arg);
		} catch (Exception e) {
			return null;
		}
	}

	public static Double string2Double(String arg) {
		try {
			return Double.parseDouble(arg);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 随机字母或数字
	 * 
	 * @param bit 位数
	 * @return
	 */
	public static String getRandom(int bit) {
		return getRandom(bit, false);
	}

	/**
	 * 随机字母或数字
	 * 
	 * @param bit        位数
	 * @param onlyNumber 是否仅数字
	 * @return
	 */
	public static String getRandom(int bit, boolean onlyNumber) {
		if (bit <= 0) {
			return "";
		}

		// 递归
		if (bit > 1) {
			return getRandom(1, onlyNumber) + getRandom(bit - 1, onlyNumber);
		}

		// 获取随机数
		int random = new Random().nextInt(onlyNumber ? 10 : 62);

		// 小写英文
		if (random >= 36) {
			return String.valueOf((char) (random + 61));
		}

		// 大写英文
		if (random >= 10) {
			return String.valueOf((char) (random + 55));
		}

		// 数字
		return String.valueOf(random);
	}

	private DataUtils() {
	}
}
