package com.kovizone.admin.service;

import java.util.List;

import com.kovizone.admin.vo.Menu;
import com.kovizone.admin.vo.TableData;
import com.kovizone.admin.po.SystemPermission;

/**
 * 系统权限服务接口
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-08 KoviChen 新建类
 */
public interface SystemPermissionService {

	/**
	 * 生成JSON格式功能菜单
	 *
	 * @param systemPermissionList 生成功能菜单基础数据集合
	 * @return 功能菜单JSON
	 */
	Menu buildMenu(List<SystemPermission> systemPermissionList);

	/**
	 * 根据符合systemPermission的非空值获取权限数据集
	 *
	 * @param systemPermission 匹配对象
	 * @param startNum         分页开始行
	 * @param size             分页大小
	 * @return 权限数据集
	 */
	TableData<SystemPermission> tableData(SystemPermission systemPermission, int startNum, int size);

	/**
	 * 保存
	 *
	 * @param systemPermission 数据对象
	 * @return 影响的行数
	 */
	int save(SystemPermission systemPermission);

	/**
	 * 更新（根据fno）
	 *
	 * @param systemPermission 数据对象
	 * @return 影响的行数
	 */
	int update(SystemPermission systemPermission);

	/**
	 * 删除
	 *
	 * @param fno 权限编号
	 * @return 影响的行数
	 */
	int remove(int fno);

	/**
	 * 根据角色编号获取权限数据集<br>
	 * 追踪父级角色编号的权限
	 *
	 * @param rno 角色编号
	 * @return 权限数据集
	 */
	List<SystemPermission> listContainParentByRno(int rno);

	/**
	 * 根据用户编号获取权限数据集<br>
	 * 追踪父级角色编号的权限
	 *
	 * @param uno 用户编号
	 * @return 权限数据集
	 */
	List<SystemPermission> listContainParentByUno(int uno);

	/**
	 * 根据用户名获取权限数据集<br>
	 * 追踪父级角色编号的权限
	 *
	 * @param uname 用户名
	 * @return 权限数据集
	 */
	List<SystemPermission> listContainParentByUname(String uname);

	/**
	 * 根据权限编号获取权限数据集<br>
	 * 追踪父级角色编号的权限
	 *
	 * @param pno 权限编号
	 * @return 权限数据集
	 */
	List<SystemPermission> listContainParentByPno(int pno);

}
