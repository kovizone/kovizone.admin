package com.kovizone.admin.service.impl;

import com.kovizone.admin.constant.CodeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kovizone.admin.vo.TableData;
import com.kovizone.admin.mapper.SystemRoleMapper;
import com.kovizone.admin.mapper.SystemRolePermissionMapper;
import com.kovizone.admin.mapper.SystemUserRoleMapper;
import com.kovizone.admin.po.SystemRole;
import com.kovizone.admin.po.SystemUserRole;
import com.kovizone.admin.service.SystemRoleService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统角色服务实现类
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-12 KoviChen 新建类
 */
@Service
public class SystemRoleServiceImpl implements SystemRoleService {

    private SystemRoleMapper systemRoleMapper;

    private SystemRolePermissionMapper systemRolePermissionMapper;

    private SystemUserRoleMapper systemUserRoleMapper;

    @Autowired(required = false)
    public SystemRoleServiceImpl(SystemRoleMapper systemRoleMapper, SystemRolePermissionMapper systemRolePermissionMapper,
                                 SystemUserRoleMapper systemUserRoleMapper) {
        this.systemRoleMapper = systemRoleMapper;
        this.systemRolePermissionMapper = systemRolePermissionMapper;
        this.systemUserRoleMapper = systemUserRoleMapper;
    }

    @Override
    public SystemRole getByRno(int rno) {
        return systemRoleMapper.getByRno(rno);
    }

    @Override
    public TableData<SystemRole> tableData(SystemRole systemRole, int startNum, int size) {
        TableData<SystemRole> list = new TableData<>();
        list.setData(systemRoleMapper.list(systemRole, startNum, size));
        list.setCount(systemRoleMapper.count(systemRole));
        return list;
    }

    @Override
    public int save(SystemRole systemRole) {
        return systemRoleMapper.save(systemRole);
    }

    @Override
    public int update(SystemRole systemRole) {
        return systemRoleMapper.update(systemRole);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int remove(int rno) {
        try {
            SystemUserRole systemUserRole = new SystemUserRole();
            systemUserRole.setRno(rno);
            List<SystemUserRole> systemUserRoles = systemUserRoleMapper.list(systemUserRole);
            if (systemUserRoles != null && !systemUserRoles.isEmpty()) {
                return CodeConstant.ROLE_NOT_FOUND;
            }
            systemRolePermissionMapper.removeByRno(rno);
            return systemRoleMapper.remove(rno);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SystemRole> listByUno(int uno) {
        return systemRoleMapper.listByUno(uno);
    }
}
