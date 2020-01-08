package com.kovizone.admin.mapper;

import org.apache.ibatis.annotations.Param;

import com.kovizone.admin.po.SystemRolePermission;

import java.util.List;

/**
 * 系统角色关联权限DAO接口
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-08 KoviChen 新增类
 */
public interface SystemRolePermissionMapper {

    /**
     * 为角色授予权限
     *
     * @param rno 角色编号
     * @param pno 权限编号
     * @return 影响行数
     */
    int grant(@Param("rno") int rno, @Param("pno") int pno);

    /**
     * 为角色废除权限
     *
     * @param rno 角色编号
     * @param pno 权限编号
     * @return 影响行数
     */
    int revoke(@Param("rno") int rno, @Param("pno") int pno);

    /**
     * 删除（根据rno）
     *
     * @param rno 角色编号
     * @return 影响行数
     */
    int removeByRno(int rno);

    /**
     * 删除（根据pno）
     *
     * @param pno 权限编号
     * @return 影响行数
     */
    int removeByPno(int pno);

    /**
     * 根据符合systemRolePermission的非空值获取角色权限关联数据集
     *
     * @param systemRolePermission 匹配对象
     * @return 角色权限关联数据集
     */
    List<SystemRolePermission> list(SystemRolePermission systemRolePermission);

}
