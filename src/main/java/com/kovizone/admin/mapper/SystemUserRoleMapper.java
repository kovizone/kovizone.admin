package com.kovizone.admin.mapper;

import org.apache.ibatis.annotations.Param;

import com.kovizone.admin.po.SystemUserRole;

import java.util.List;

/**
 * 系统用户关联角色DAO接口
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-08 KoviChen 新增类
 */
public interface SystemUserRoleMapper {

    /**
     * 为用户授予角色
     *
     * @param uno 用户编号
     * @param rno 角色编号
     * @return 影响行数
     */
    int grant(@Param("uno") int uno, @Param("rno") int rno);

    /**
     * 为用户废除权限
     *
     * @param uno 用户编号
     * @param rno 角色编号
     * @return 影响行数
     */
    int revoke(@Param("uno") int uno, @Param("rno") int rno);

    /**
     * 删除（根据uno）
     *
     * @param uno 用户编号
     * @return 影响行数
     */
    int removeByUno(int uno);

    /**
     * 删除（根据rno）
     *
     * @param rno 角色编号
     * @return 影响行数
     */
    int removeByRno(int rno);

    /**
     * 根据符合systemUserRole的非空值获取用户角色关联数据集
     *
     * @param systemUserRole 匹配对象
     * @return 角色关联数据集
     */
    List<SystemUserRole> list(SystemUserRole systemUserRole);

}
