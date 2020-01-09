package com.kovizone.admin.service;

import java.util.List;

import com.kovizone.admin.vo.TableData;
import com.kovizone.admin.po.SystemRole;

/**
 * 系统角色服务接口
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-12 KoviChen 新建类
 */
public interface SystemRoleService {

	/**
	 * 根据角色编号获取角色数据
	 *
	 * @param rno 角色编号
	 * @return 角色数据
	 */
	SystemRole getByRno(int rno);

	/**
	 * 根据符合systemGroup的非空值获取角色数据集
	 *
	 * @param systemRole 匹配对象
	 * @param startNum   分页开始行
	 * @param size       分页大小
	 * @return 角色数据集
	 */
	TableData<SystemRole> tableData(SystemRole systemRole, int startNum, int size);

	/**
	 * 保存
	 *
	 * @param systemRole 保存数据
	 * @return 影响的行数
	 */
	int save(SystemRole systemRole);

	/**
	 * 更新（根据rno）
	 *
	 * @param systemRole 更新数据
	 * @return 影响的行数
	 */
	int update(SystemRole systemRole);

	/**
	 * 删除
	 *
	 * @param rno 角色编号
	 * @return 影响的行数
	 */
	int remove(int rno);

	/**
	 * 根据用户编号获取角色数据集
	 *
	 * @param uno 用户编号
	 * @return 角色数据集
	 */
	List<SystemRole> listByUno(int uno);
}
