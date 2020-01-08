package com.kovizone.admin.util;

import com.alibaba.fastjson.JSONObject;

/**
 * JSON工具类
 * 
 * @author Kovi(kovichen@163.com)
 * @version 0.0.1 20191113 KoviChen 新增JSONObject build(Object... arg)
 */
public class JSONUtils {

	public static void main(String[] args) {
		JSONObject jsonObject = JSONUtils.build(
				"key1", "value1",
				"key2", "value2",
				"key3", 3);
		// 输出 {"key1":"value1","key2":"value2","key3":3}
		System.out.println(jsonObject.toJSONString());
	}

	/**
	 * 快速构建JsonObject<BR>
	 * 示例：<BR>
	 * JSONObject jsonObject = JSONUtils.build(<BR>
	 * &ensp;&ensp;&ensp;&ensp;"key1", "value1",<BR>
	 * &ensp;&ensp;&ensp;&ensp;"key2", "value2",<BR>
	 * &ensp;&ensp;&ensp;&ensp;"key3", 3);<BR>
	 * 输出 {"key1":"value1","key2":"value2","key3":3}
	 * 
	 * @param arg 不定参数，按照索引识别为Key和Value，<BR>
	 *            偶数和0索引的值为Key，奇数索引的值为Value
	 * @return JSON对象
	 */
	public static JSONObject build(Object... arg) {
		JSONObject jsonObject = new JSONObject();
		String key = null;
		for (int i = 0; i < arg.length; i++) {
			Object o = arg[i];
			if (i % 2 == 0) {
				key = String.valueOf(o);
			} else {
				if (key != null) {
					jsonObject.put(key, o);
				}
			}
		}
		return jsonObject;
	}

}
