package com.kovizone.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kovizone.admin.po.SystemPermission;

/**
 * 系统权限DAO接口
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-08 KoviChen 新增类
 */
public interface SystemPermissionMapper {

	/**
	 * 根据符合systemPermission的非空值获取权限数据集
	 *
	 * @param systemPermission 匹配对象
	 * @param startNum         分页开始行
	 * @param size             分页大小
	 * @return 权限数据集
	 */
	List<SystemPermission> list(@Param("systemPermission") SystemPermission systemPermission, @Param("startNum") int startNum, @Param("size") int size);

	/**
	 * 获取权限请求地址集
	 *
	 * @return 权限请求地址集
	 */
	List<String> listUrl();

	/**
	 * 统计
	 *
	 * @param systemPermission 匹配对象
	 * @return 行数
	 */
	int count(SystemPermission systemPermission);

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
	 *
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
	 * 根据角色编号获取权限数据集
	 *
	 * @param rnos 角色编号数角色
	 * @return 权限数据集
	 */
	List<SystemPermission> listByRnos(int[] rnos);

	/**
	 * 根据权限编号获取权限数据集<br>
	 * 追踪父级角色编号的权限
	 *
	 * @param pno 权限编号
	 * @return 权限数据集
	 */
	List<SystemPermission> listContainParentByPno(int pno);
}