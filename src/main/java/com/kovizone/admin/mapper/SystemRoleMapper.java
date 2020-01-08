package com.kovizone.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kovizone.admin.po.SystemRole;

/**
 * 系统角色DAO接口
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-08 KoviChen 新增类
 */
public interface SystemRoleMapper {

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
	List<SystemRole> list(@Param("systemRole") SystemRole systemRole, @Param("startNum") int startNum, @Param("size") int size);

	/**
	 * 统计
	 *
	 * @param systemRole 匹配对象
	 * @return 行数
	 */
	int count(SystemRole systemRole);

	/**
	 * 保存
	 *
	 * @param systemRole 保存数据
	 * @return 影响的行数
	 */
	int save(SystemRole systemRole);

	/**
	 * 更新（根据Rno）
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

	/**
	 * 根据角色编号获取角色数据集<br>
	 * 追踪父级角色编号的角色
	 *
	 * @param rno 角色编号
	 * @return 角色数据集
	 */
	List<SystemRole> listContainParentByRno(int rno);
}
