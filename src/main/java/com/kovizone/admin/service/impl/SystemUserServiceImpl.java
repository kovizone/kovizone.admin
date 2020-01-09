package com.kovizone.admin.service.impl;

import com.kovizone.admin.constant.CodeConstant;
import com.kovizone.admin.constant.NumberConstant;
import com.kovizone.admin.mapper.SystemUserMapper;
import com.kovizone.admin.mapper.SystemUserRoleMapper;
import com.kovizone.admin.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.kovizone.admin.vo.TableData;
import com.kovizone.admin.po.SystemUser;
import com.kovizone.admin.util.DataUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 系统用户服务实现类
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-13 KoviChen 新建类
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {

    private SystemUserMapper systemUserMapper;

    private SystemUserRoleMapper systemUserRoleMapper;

    @Autowired(required = false)
    public SystemUserServiceImpl(SystemUserMapper systemUserMapper, SystemUserRoleMapper systemUserRoleMapper) {
        this.systemUserMapper = systemUserMapper;
        this.systemUserRoleMapper = systemUserRoleMapper;
    }

    @Override
    public SystemUser getByUno(String uno) {
        return systemUserMapper.getByUno(uno);
    }

    @Override
    public SystemUser getByUname(String uname) {
        return systemUserMapper.getByUname(uname);
    }

    @Override
    public TableData<SystemUser> tableData(SystemUser systemUser, int startNum, int size) {
        TableData<SystemUser> list = new TableData<>();
        list.setData(systemUserMapper.list(systemUser, startNum, size));
        int c = systemUserMapper.count(systemUser);
        list.setCount(c);
        return list;
    }

    @Override
    public Integer save(SystemUser systemUser, int createrno, String checkPassword, int[] rnos) {

        if (!systemUser.getPassword().equalsIgnoreCase(checkPassword)) {
            return CodeConstant.PASSWORD_ERROR;
        }
        String newSalt = DataUtils.getRandom(NumberConstant.SALT_LENGTH);
        systemUser.setPassword(saltPassword(systemUser.getPassword().toUpperCase(), newSalt));
        systemUser.setSalt(newSalt);
        systemUser.setCreaterno(createrno);
        int result = systemUserMapper.save(systemUser);

        if (rnos != null) {
            List<SystemUser> systemUserList = systemUserMapper.list(systemUser, 0, 1);
            for (int rno : rnos) {
                result += systemUserRoleMapper.grant(systemUserList.get(0).getUno(), rno);
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(SystemUser systemUser, int[] rnos) {
        try {
            int result = systemUserMapper.update(systemUser);
            if (rnos != null) {
                systemUserRoleMapper.removeByUno(systemUser.getUno());
                for (int rno : rnos) {
                    result += systemUserRoleMapper.grant(systemUser.getUno(), rno);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int remove(int uno) {
        try {
            if (uno == 1) {
                // 不允许删除admin
                return CodeConstant.PROHIBIT_DELETION_ADMIN;
            }
            systemUserRoleMapper.removeByUno(uno);
            return systemUserMapper.remove(uno);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkPassword(String basePassword, String userPassword, String salt) {
        if (salt != null && salt.length() == NumberConstant.SALT_LENGTH) {
            return basePassword.equals(saltPassword(userPassword, salt));
        }

        // 没有盐，简单比较MD5
        String md5UserPassword = DigestUtils.md5DigestAsHex(userPassword.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return basePassword.equals(md5UserPassword);
    }

    @Override
    public String saltPassword(String password, String salt) {
        if (salt == null || salt.length() != NumberConstant.SALT_LENGTH) {
            return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        }
        StringBuilder sb = new StringBuilder(password);
        sb.insert(24, salt.substring(0, 4));
        sb.insert(20, salt.substring(4, 8));
        sb.insert(16, salt.substring(8, 12));
        sb.insert(12, salt.substring(12, 16));
        sb.insert(8, salt.substring(16, 20));
        sb.insert(4, salt.substring(20, 24));
        sb.insert(0, salt.substring(24, 32));
        // 加盐后MD5加密
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }
}
