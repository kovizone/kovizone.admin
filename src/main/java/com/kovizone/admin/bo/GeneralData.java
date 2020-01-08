package com.kovizone.admin.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用数据结果<br>
 * 半强制：所有@ResponseBody注解的方法或所有@RestController注解类中的方法都应当返回该类
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-09 KoviChen 新建类
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class GeneralData {

	/**
	 * 服务结果
	 */
	private boolean result;

	/**
	 * 服务响应码<BR/>
	 * 403 - 会话超时
	 */
	private int code;

	/**
	 * 服务响应信息
	 */
	private String msg;

	/**
	 * 存储List集
	 */
	private List list;

	/**
	 * 存储Map集
	 */
	private Map map;

	public GeneralData(boolean result, int code, String msg) {
		this.result = result;
		this.code = code;
		this.msg = msg;
	}

	public GeneralData(boolean result, String msg) {
		this.result = result;
		this.msg = msg;
	}

	public GeneralData(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public GeneralData() {
		this.result = true;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void addMap(Object key, Object value) {
		if (map == null) {
			map = new HashMap(16);
		}
		map.put(key, value);
	}
}
