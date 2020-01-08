package com.kovizone.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kovizone.admin.po.SystemUser;

/**
 * 系统用户DAO接口
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-08 KoviChen 新增类
 */
public interface SystemUserMapper {

	/**
	 * 根据uno获取用户数据
	 *
	 * @param uno 用户编号
	 * @return 用户数据
	 */
	SystemUser getByUno(String uno);

	/**
	 * 根据uname获取用户数据
	 *
	 * @param uname 用户名
	 * @return 用户数据
	 */
	SystemUser getByUname(String uname);

	/**
	 * 根据符合systemUser的非空值获取用户数据集
	 *
	 * @param systemUser 匹配对象
	 * @param startNum   分页开始行
	 * @param size       分页大小
	 * @return 用户数据集
	 */
	List<SystemUser> list(@Param("systemUser") SystemUser systemUser, @Param("startNum") int startNum, @Param("size") int size);

	/**
	 * 统计
	 *
	 * @param systemUser 匹配对象
	 * @return 行数
	 */
	int count(SystemUser systemUser);

	/**
	 * 保存
	 *
	 * @param systemUser 保存数据
	 * @return 影响的行数
	 */
	int save(SystemUser systemUser);

	/**
	 * 更新（根据uno和uname）
	 *
	 * @param systemUser 更新数据
	 * @return 影响的行数
	 */
	int update(SystemUser systemUser);

	/**
	 * 删除
	 *
	 * @param uno 用户编号
	 * @return 影响的行数
	 */
	int remove(int uno);

}
