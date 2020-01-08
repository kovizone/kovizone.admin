package com.kovizone.admin.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Map工具集
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-06 新建类
 */
public class MapUtils {

	public static <K, V> Map<K, V> buildMap(K key, V value) {
		Map<K, V> map = new HashMap<K, V>();
		map.put(key, value);
		return map;
	}

}
