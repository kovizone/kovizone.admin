package com.kovizone.admin.service;

import java.util.List;

import com.kovizone.admin.po.SystemRolePermission;

/**
 * 系统角色关联权限服务接口
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-12 KoviChen 新建类
 */
public interface SystemRolePermissionService {

	/**
	 * 为角色授予权限<br>
	 * 废除其他权限
	 *
	 * @param rno  角色编号
	 * @param pnos 权限编号数组
	 * @return 授权数
	 */
	int grant(int rno, Integer[] pnos);

	/**
	 * 根据符合systemPermissionGroup的非空值获取角色数据集
	 *
	 * @param systemRolePermission 匹配对象
	 * @return 角色数据集
	 */
	List<SystemRolePermission> list(SystemRolePermission systemRolePermission);

}
