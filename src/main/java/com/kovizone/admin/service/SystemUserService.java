package com.kovizone.admin.service;

import com.kovizone.admin.bo.TableData;
import com.kovizone.admin.po.SystemUser;

/**
 * 系统用户服务接口
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-13 KoviChen 新建类
 */
public interface SystemUserService {

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
	TableData<SystemUser> tableData(SystemUser systemUser, int startNum, int size);

	/**
	 * 保存
	 *
	 * @param systemUser    保存数据
	 * @param createrno     创建者编号
	 * @param checkPassword 验证密码
	 * @param rnos          角色编号集
	 * @return 影响的行数
	 */
	Integer save(SystemUser systemUser, int createrno, String checkPassword, int[] rnos);

	/**
	 * 更新（根据uno）
	 *
	 * @param systemUser 更新数据
	 * @param rnos       角色编号集
	 * @return 影响的行数
	 */
	int update(SystemUser systemUser, int[] rnos);

	/**
	 * 删除
	 *
	 * @param uno 用户编号
	 * @return 影响的行数
	 */
	int remove(int uno);

	/**
	 * 校验密码
	 * 
	 * @param basePassword 库中密码
	 * @param userPassword 用户输入密码（前端经过加密）
	 * @param salt         密码盐，必须32位
	 * @return 密码是否正确
	 */
	boolean checkPassword(String basePassword, String userPassword, String salt);

	/**
	 * 密码加盐加密
	 * 
	 * @param password 密码
	 * @param salt     盐
	 * @return 结果
	 */
	String saltPassword(String password, String salt);

}
