package com.kovizone.admin.service;

import java.util.List;

import com.kovizone.admin.po.SystemUserRole;

/**
 * 系统用户关联角色服务接口
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-19 KoviChen 新建类
 */
public interface SystemUserRoleService {

	/**
	 *
	 /**
	 * 为用户授予角色<br>
	 * 废除其他角色
	 *
	 * @param uno  用户编号
	 * @param rnos 角色编号数组
	 * @return 授权数
	 */
	int grant(int uno, Integer[] rnos);

	/**
	 * 根据符合systemUserRole的非空值获取角色数据集
	 *
	 * @param systemUserRole 匹配对象
	 * @return 用户角色集
	 */
	List<SystemUserRole> list(SystemUserRole systemUserRole);

}
