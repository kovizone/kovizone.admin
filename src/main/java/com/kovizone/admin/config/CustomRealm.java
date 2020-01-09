package com.kovizone.admin.config;

import com.kovizone.admin.service.SystemPermissionService;
import com.kovizone.admin.service.SystemUserRoleService;
import com.kovizone.admin.service.SystemUserService;
import com.kovizone.admin.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.kovizone.admin.po.SystemPermission;
import com.kovizone.admin.po.SystemUser;
import com.kovizone.admin.po.SystemUserRole;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义领域
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-07 KoviChen 新建类
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    private SystemUserService systemUserService;

    private SystemUserRoleService systemUserRoleService;

    private SystemPermissionService systemPermissionService;

    @Autowired
    private CustomRealm(
    		SystemUserService systemUserService,
			SystemPermissionService systemPermissionService,
			SystemUserRoleService systemUserRoleService) {
        this.systemUserService = systemUserService;
		this.systemPermissionService = systemPermissionService;
		this.systemUserRoleService = systemUserRoleService;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        SystemUser systemUser = systemUserService.getByUname(token.getUsername());
        if (null == systemUser) {
            throw new AccountException("用户名不存在");
        }

        // 加盐加密认证
        String password = new String((char[]) token.getCredentials());
        if (!systemUserService.checkPassword(systemUser.getPassword(), password, systemUser.getSalt())) {
            throw new AccountException("用户名或密码不正确");
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        SystemUser systemUser = systemUserService.getByUname(username);
        SystemUserRole systemUserRole = new SystemUserRole();
        systemUserRole.setUno(systemUser.getUno());
        List<SystemUserRole> systemUserRoles = systemUserRoleService.list(systemUserRole);
        for (SystemUserRole sur : systemUserRoles) {
            roles.add(String.valueOf(sur.getRno()));

            List<SystemPermission> systemPermissionList = systemPermissionService.listContainParentByRno(sur.getRno());
            if (systemPermissionList != null) {
                for (SystemPermission systemPermission : systemPermissionList) {
                    String url = systemPermission.getUrl();
                    if (!StringUtils.isEmpty(url)) {
                        permissions.add(systemPermission.getUrl().trim());
                    }
                }
            }
        }

        // 设置角色（角色编号）
        simpleAuthorizationInfo.setRoles(roles);

        // 设置权限（URL）
        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }
}