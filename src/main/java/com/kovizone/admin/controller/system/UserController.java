package com.kovizone.admin.controller.system;

import com.kovizone.admin.constant.*;
import com.kovizone.admin.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.kovizone.admin.anno.PermissionScanIgnore;
import com.kovizone.admin.vo.GeneralData;
import com.kovizone.admin.vo.TableData;
import com.kovizone.admin.po.SystemUser;
import com.kovizone.admin.service.SystemRoleService;
import com.kovizone.admin.service.SystemUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户控制
 * <p/>
 * URL字典
 * <TR>
 * <TD>/user/view.do</TD>
 * <TD>跳转到用户管理页</TD>
 * </TR>
 * <TR>
 * <TD>/user/save.do</TD>
 * <TD>新增/保存新用户</TD>
 * </TR>
 * <TR>
 * <TD>/user/update.do</TD>
 * <TD>更新/编辑用户</TD>
 * </TR>
 * <TR>
 * <TD>/user/list.do</TD>
 * <TD>获取用户数据集</TD>
 * </TR>
 * <TR>
 * <TD>/user/remove.do</TD>
 * <TD>删除用户</TD>
 * </TR>
 * <TR>
 * <TD>/user/updatePassowrd.do</TD>
 * <TD>验证旧密码等更新/修改密码</TD>
 * </TR>
 * <TR>
 * <TD>/user/resetPassowrd.do</TD>
 * <TD>重置密码，无需验证旧密码等</TD>
 * </TR>
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-09 KoviChen 新建类
 */
@RequestMapping("/user")
@Controller
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private SystemUserService systemUserService;

    private SystemRoleService systemRoleService;

    @Value("${login.rsa.private.key}")
    private String privateKey;

    @Autowired
    public UserController(SystemUserService systemUserService, SystemRoleService systemRoleService) {
        this.systemUserService = systemUserService;
        this.systemRoleService = systemRoleService;
    }

    /**
     * 验证用户名和密码
     *
     * @param request 请求
     * @return GeneralData
     */
    @PermissionScanIgnore(loginRequired = false)
    @PostMapping("/login.do")
    @ResponseBody
    public GeneralData login(HttpServletRequest request) {

        // shiro 身份认证
        try {
            String username = request.getParameter(ParameterConstant.USERNAME);
            String password = request.getParameter(ParameterConstant.PASSWORD);
            String verify = request.getParameter(ParameterConstant.VERIFY);
            String random = (String) request.getSession().getAttribute(RandomValidateCodeUtil.RANDOM_VALIDATE_CODE_KEY);
            if (StringUtils.isBlank(verify)) {
                return new GeneralData(false, "请输入验证码");
            }
            if (!RandomValidateCodeUtil.verifyRandomCode(verify, random)) {
                return new GeneralData(false, "请输入正确的验证码");
            }
            //RAS解密
            password = RSAUtils.decode(password, privateKey);
            password = MD5Utils.encode(password);
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        } catch (AuthenticationException e) {
            logger.info("{}:{}", HttpUtils.getSessionSimpleId(request.getSession(false)), e.getMessage());
            return new GeneralData(false, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new GeneralData(false, MessageConstant.EXCEPTION);
        }

        return new GeneralData(true, MessageConstant.LOGIN_SUCCESS);
    }

    @RequestMapping(value = "/view.do")
    public ModelAndView view() {
        ModelAndView mv = new ModelAndView("system/user");
        List<?> systemRoleList = systemRoleService.tableData(null, 0, 0).getData();
        mv.addObject("systemRoleList", systemRoleList);
        mv.addObject("now", new Date());
        return mv;
    }

    @PostMapping("/save.do")
    @ResponseBody
    public GeneralData save(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                return null;
            }
            Subject subject = SecurityUtils.getSubject();
            SystemUser systemUser = (SystemUser) SecurityUtils.getSubject().getPrincipal();
            SystemUser systemUserSaveBean = DataUtils.request2Object(request, SystemUser.class);
            String rnosStr = request.getParameter("rno");
            if (rnosStr == null || "".equals(rnosStr)) {
                return new GeneralData(false, MessageConstant.NO_SET_ROLE);
            }
            String[] rnoStrs = rnosStr.split(",");
            int[] rnos = new int[rnoStrs.length];
            for (int i = 0; i < rnoStrs.length; i++) {
                rnos[i] = Integer.parseInt(rnoStrs[i]);
            }

            Integer result = systemUserService.save(systemUserSaveBean, systemUser.getUno(), request.getParameter("checkPassword"), rnos);
            if (result == null) {
                return new GeneralData(false, MessageConstant.SAVE_FAIL);
            }
            if (result > 0) {
                return new GeneralData(true, MessageConstant.SAVE_SUCCESS);
            }
            if (result == CodeConstant.CHECK_PASSWORD_FAIL) {
                return new GeneralData(false, MessageConstant.CHECK_PASSWORD_FAIL);
            }
            if (result == CodeConstant.USER_NAME_EXISTS) {
                return new GeneralData(false, MessageConstant.USER_NAME_EXISTS);
            }
            return new GeneralData(false, MessageConstant.SAVE_FAIL + "（" + result + "）");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new GeneralData(false, MessageConstant.SAVE_FAIL);
        }
    }

    @PostMapping("/update.do")
    @ResponseBody
    public GeneralData update(HttpServletRequest request) {
        try {
            SystemUser systemUser = DataUtils.request2Object(request, SystemUser.class);
            // 不允许修改密码
            systemUser.setPassword(null);

            String rnosStr = request.getParameter("rno");
            if (rnosStr == null || "".equals(rnosStr)) {
                return new GeneralData(false, MessageConstant.GRANT_FAIL);
            }
            String[] rnoStrs = rnosStr.split(",");
            int[] rnos = new int[rnoStrs.length];
            for (int i = 0; i < rnoStrs.length; i++) {
                rnos[i] = Integer.parseInt(rnoStrs[i]);
            }

            if (systemUserService.update(systemUser, rnos) > 0) {
                return new GeneralData(true, MessageConstant.UPDATE_SUCCESS);
            }
            return new GeneralData(false, MessageConstant.UPDATE_FAIL);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new GeneralData(false, MessageConstant.EXCEPTION);
        }
    }

    @PermissionScanIgnore
    @PostMapping("/updatePassword.do")
    @ResponseBody
    public GeneralData updatePassword(HttpServletRequest request) {
        try {
            String uname = request.getParameter("uname");
            String oldPassword = request.getParameter("oldPassword").toUpperCase();
            String newPassword1 = request.getParameter("newPassword1").toUpperCase();
            String newPassword2 = request.getParameter("newPassword2").toUpperCase();

            if (ParameterConstant.GUEST.equals(uname)) {
                return new GeneralData(false, MessageConstant.GUEST_UPDATE_FAIL);
            }

            if (!newPassword1.equals(newPassword2)) {
                return new GeneralData(false, MessageConstant.CHECK_PASSWORD_FAIL);
            }
            SystemUser systemUser = systemUserService.getByUname(uname);
            if (systemUser == null) {
                return new GeneralData(false, MessageConstant.USER_NOT_EXIST);
            }
            if (!systemUserService.checkPassword(systemUser.getPassword(), oldPassword, systemUser.getSalt())) {
                return new GeneralData(false, MessageConstant.CHECK_OLD_PASSWORD_FAIL);
            }

            // 更新密码盐
            String newSalt = DataUtils.getRandom(NumberConstant.SALT_LENGTH);
            systemUser.setPassword(systemUserService.saltPassword(newPassword1, newSalt));
            systemUser.setSalt(newSalt);
            int result = systemUserService.update(systemUser, null);
            if (result > 0) {
                return new GeneralData(true, MessageConstant.UPDATE_SUCCESS);
            }
            return new GeneralData(false, MessageConstant.UPDATE_FAIL + "（" + result + "）");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new GeneralData(false, MessageConstant.EXCEPTION);
        }
    }

    @PostMapping("/resetPassword.do")
    @ResponseBody
    public GeneralData resetPassword(HttpServletRequest request) {
        try {
            String uname = request.getParameter("uname");
            String newPassword1 = request.getParameter("newPassword1").toUpperCase();
            String newPassword2 = request.getParameter("newPassword2").toUpperCase();

            if (!newPassword1.equals(newPassword2)) {
                return new GeneralData(false, MessageConstant.CHECK_PASSWORD_FAIL);
            }
            SystemUser systemUser = systemUserService.getByUname(uname);
            if (systemUser == null) {
                return new GeneralData(false, MessageConstant.USER_NOT_EXIST);
            }

            // 更新密码盐
            String newSalt = DataUtils.getRandom(NumberConstant.SALT_LENGTH);
            systemUser.setPassword(systemUserService.saltPassword(newPassword1, newSalt));
            systemUser.setSalt(newSalt);
            int result = systemUserService.update(systemUser, null);
            if (result > 0) {
                return new GeneralData(true, MessageConstant.UPDATE_SUCCESS);
            }
            return new GeneralData(false, MessageConstant.UPDATE_FAIL + "（" + result + "）");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new GeneralData(false, MessageConstant.EXCEPTION);
        }
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public TableData<SystemUser> list(HttpServletRequest request) {
        SystemUser systemUser = DataUtils.request2Object(request, SystemUser.class);
        int page = Integer.parseInt(request.getParameter(ParameterConstant.PAGE));
        int limit = Integer.parseInt(request.getParameter(ParameterConstant.LIMIT));

        int startNum = (page - 1) * limit;

        TableData<SystemUser> tableData = systemUserService.tableData(systemUser, startNum, limit);
        tableData.setCode(0);
        tableData.setMsg("");
        return tableData;
    }

    @PostMapping("/remove.do")
    @ResponseBody
    public GeneralData remove(HttpServletRequest request) {
        try {
            int uno = Integer.parseInt(request.getParameter("uno"));
            if (systemUserService.remove(uno) > 0) {
                return new GeneralData(false, MessageConstant.REMOVE_SUCCESS);
            }
            if (systemUserService.remove(uno) == CodeConstant.PROHIBIT_DELETION_ADMIN) {
                return new GeneralData(false, MessageConstant.PROHIBIT_DELETION_ADMIN);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new GeneralData(false, MessageConstant.EXCEPTION);
        }
        return new GeneralData(false, MessageConstant.REMOVE_FAIL);
    }

}
