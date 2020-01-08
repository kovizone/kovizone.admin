package com.kovizone.admin.bo;

import java.util.List;

/**
 * 表格数据，支持layui_table
 * 
 * @author KoviChen
 * @version 0.0.1 2019-08-14 新建类
 */
public class TableData<T> {
	/**
	 * 响应码，默认0
	 */
	private Integer code;

	/**
	 * 响应信息
	 */
	private String msg;

	/**
	 * 数据统计
	 */
	private long count;

	/**
	 * 数据集合
	 */
	private List<T> data;

	public TableData() {
		super();
		this.code = 0;
		this.msg = "";
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
