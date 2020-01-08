package com.kovizone.admin.service.impl;

import com.kovizone.admin.po.SystemRole;
import com.kovizone.admin.po.SystemUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kovizone.admin.mapper.SystemRoleMapper;
import com.kovizone.admin.mapper.SystemUserRoleMapper;
import com.kovizone.admin.service.SystemUserRoleService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户关联角色服务实现类
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-12 KoviChen 新建类
 */
@Service
public class SystemUserRoleServiceImpl implements SystemUserRoleService {

    private SystemUserRoleMapper systemUserRoleMapper;

    private SystemRoleMapper systemRoleMapper;

    @Autowired(required = false)
    public SystemUserRoleServiceImpl(SystemUserRoleMapper systemUserRoleMapper, SystemRoleMapper systemRoleMapper) {
        this.systemUserRoleMapper = systemUserRoleMapper;
        this.systemRoleMapper = systemRoleMapper;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public int grant(int uno, Integer[] rnos) {
        try {
            systemUserRoleMapper.removeByRno(uno);
            int num = 1;

            if (rnos != null && rnos.length != 0) {
                List<Integer> rnoList = new ArrayList<>();
                for (Integer rno : rnos) {
                    List<SystemRole> systemRoles = systemRoleMapper.listContainParentByRno(rno);
                    for (SystemRole systemRole : systemRoles) {
                        if (!rnoList.contains(systemRole.getRno())) {
                            rnoList.add(systemRole.getRno());
                        }
                    }
                }
                for (Integer rno : rnoList) {
                    num += systemUserRoleMapper.grant(uno, rno);
                }
            }
            return num;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SystemUserRole> list(SystemUserRole systemUserRole) {
        return systemUserRoleMapper.list(systemUserRole);
    }
}
