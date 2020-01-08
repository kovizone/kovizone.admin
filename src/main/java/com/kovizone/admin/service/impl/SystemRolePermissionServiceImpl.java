package com.kovizone.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kovizone.admin.mapper.SystemPermissionMapper;
import com.kovizone.admin.mapper.SystemRolePermissionMapper;
import com.kovizone.admin.po.SystemPermission;
import com.kovizone.admin.po.SystemRolePermission;
import com.kovizone.admin.service.SystemRolePermissionService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统角色关联权限服务实现类
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-12 KoviChen 新建类
 */
@Service
public class SystemRolePermissionServiceImpl implements SystemRolePermissionService {

    private SystemRolePermissionMapper systemRolePermissionMapper;

    private SystemPermissionMapper systemPermissionMapper;

    @Autowired(required = false)
    public SystemRolePermissionServiceImpl(SystemRolePermissionMapper systemRolePermissionMapper, SystemPermissionMapper systemPermissionMapper) {
        this.systemRolePermissionMapper = systemRolePermissionMapper;
        this.systemPermissionMapper = systemPermissionMapper;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public int grant(int rno, Integer[] pnos) {
        try {
            systemRolePermissionMapper.removeByRno(rno);
            int num = 1;

            if (pnos != null && pnos.length != 0) {
                List<Integer> pnoList = new ArrayList<>();
                for (Integer pno : pnos) {
                    List<SystemPermission> systemPermissions = systemPermissionMapper.listContainParentByPno(pno);
                    for (SystemPermission systemPermission : systemPermissions) {
                        if (!pnoList.contains(systemPermission.getPno())) {
                            pnoList.add(systemPermission.getPno());
                        }
                    }
                }
                for (Integer pno : pnoList) {
                    num += systemRolePermissionMapper.grant(rno, pno);
                }
            }
            return num;
        } catch (Exception e) {
			throw new RuntimeException(e);
        }
    }

    @Override
    public List<SystemRolePermission> list(SystemRolePermission systemRolePermission) {
        return systemRolePermissionMapper.list(systemRolePermission);
    }
}
