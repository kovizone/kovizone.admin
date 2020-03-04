package com.kovizone.admin.controller.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kovizone.admin.constant.LayuiIconConstatnt;
import com.kovizone.admin.constant.ParameterConstant;
import com.kovizone.admin.po.SystemUser;
import com.kovizone.admin.util.RandomValidateCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kovizone.admin.anno.PermissionScanIgnore;
import com.kovizone.admin.vo.GeneralData;
import com.kovizone.admin.vo.Menu;
import com.kovizone.admin.vo.TableData;
import com.kovizone.admin.constant.MessageConstant;
import com.kovizone.admin.po.SystemPermission;
import com.kovizone.admin.service.SystemPermissionService;
import com.kovizone.admin.service.SystemUserService;
import com.kovizone.admin.util.DataUtils;

/**
 * 系统底层控制<BR/>
 * 仅一级请求地址
 * <p/>
 * URL字典
 * <TR>
 * <TD>/generalData.do</TD>
 * <TD>生成通用数据结果</TD>
 * </TR>
 * <TR>
 * <TD>/index.do</TD>
 * <TD>跳转到主页</TD>
 * </TR>
 * <TR>
 * <TD>/error.do</TD>
 * <TD>跳转到错误页</TD>
 * </TR>
 * <TR>
 * <TD>/menu.do</TD>
 * <TD>获取菜单</TD>
 * </TR>
 * <TR>
 * <TD>/login.do</TD>
 * <TD>跳转到登录页</TD>
 * </TR>
 * <TR>
 * <TD>/logout.do</TD>
 * <TD>注销当前用户</TD>
 * </TR>
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-06 KoviChen 新建类
 */
@Controller
public class SystemController {

    private Logger logger = LoggerFactory.getLogger(SystemController.class);

    @Value("${login.rsa.public.key}")
    private String publicKey;

    @Value("${html.title}")
    private String htmlTitle;

    @Value("${html.header.name}")
    private String htmlHeaderName;

    private SystemPermissionService systemPermissionService;

    private SystemUserService systemUserService;

    @Autowired
    public SystemController(SystemPermissionService systemPermissionService, SystemUserService systemUserService) {
        this.systemPermissionService = systemPermissionService;
        this.systemUserService = systemUserService;
    }

    @PermissionScanIgnore
    @RequestMapping("/now.do")
    @ResponseBody
    public Map<String, Object> now() {
        return new HashMap<String, Object>() {{
            put("now", new Date());
        }};
    }

    @PermissionScanIgnore(loginRequired = false)
    @RequestMapping("/generalData.do")
    @ResponseBody
    public GeneralData generalData(HttpServletRequest request) {
        String resultString = request.getParameter("result");
        boolean result = "true".equals(resultString);

        String codeString = request.getParameter("code");
        int code = codeString != null && !"".equals(codeString) ? Integer.parseInt(codeString) : 0;

        String msg = request.getParameter("msg");

        GeneralData generalData = new GeneralData();
        generalData.setResult(result);
        generalData.setCode(code);
        generalData.setMsg(msg);
        return generalData;
    }

    @PermissionScanIgnore
    @RequestMapping({"", "/index.do"})
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("index");
        Subject subject = SecurityUtils.getSubject();
        mv.addObject("testMode", "true");
        mv.addObject("systemUser", (SystemUser) SecurityUtils.getSubject().getPrincipal());
        mv.addObject("maxInactiveInterval", request.getSession(false).getMaxInactiveInterval());
        mv.addObject(ParameterConstant.HTML_TITLE, htmlTitle);
        mv.addObject(ParameterConstant.HTML_HEAD_NAME, htmlHeaderName);
        return mv;
    }

    /**
     * 全局权限<BR/>
     * 传入message，控制提示语<BR/>
     * 传入icon，控制图标
     *
     * @param request 请求
     * @return 视图
     */
    @PermissionScanIgnore(loginRequired = false)
    @RequestMapping("/error.do")
    public ModelAndView error(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String message = request.getParameter("message");
        String icon = request.getParameter("icon");

        if (DataUtils.isEmpty(message)) {
            if (session == null) {
                message = MessageConstant.THE_SESSION_IS_EXPIRED_TEXT;
            } else {
                message = MessageConstant.BAD_REQUEST;
            }
        }
        if (DataUtils.isEmpty(icon)) {
            if (session == null) {
                icon = LayuiIconConstatnt.LAYUI_ICON_UNLINK;
            } else {
                icon = LayuiIconConstatnt.LAYUI_ICON_404;
            }
        }

        ModelAndView mv = new ModelAndView("error");
        mv.addObject(ParameterConstant.MESSAGE, message);
        mv.addObject(ParameterConstant.ICON, icon);
        mv.addObject(ParameterConstant.HTML_TITLE, htmlTitle);
        return mv;
    }

    @PermissionScanIgnore(loginRequired = false)
    @RequestMapping("/menu.do")
    @ResponseBody
    public Menu menu(HttpServletRequest request) {

        String type = request.getParameter(ParameterConstant.TYPE);
        List<SystemPermission> systemPermissions = null;

        if (type == null || "".equals(type)) {
            SystemUser systemUser = (SystemUser) SecurityUtils.getSubject().getPrincipal();
            systemPermissions = systemPermissionService.listContainParentByUname(systemUser.getUname());
        }

        if ("updatePermission".equals(type)) {
            TableData<SystemPermission> tableData = systemPermissionService.tableData(null, 0, 0);
            if (tableData == null || tableData.getData() == null || tableData.getData().isEmpty()) {
                return new Menu();
            }
            systemPermissions = tableData.getData();
        }

        if (systemPermissions == null || systemPermissions.isEmpty()) {
            return new Menu();
        }
        Menu menu = systemPermissionService.buildMenu(systemPermissions);
        logger.debug("生成菜单：{}", menu);
        return menu;
    }

    @PermissionScanIgnore(loginRequired = false)
    @RequestMapping("/login.do")
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("login");
        mv.addObject(ParameterConstant.MESSAGE, request.getParameter("message"));
        mv.addObject(ParameterConstant.HTML_TITLE, htmlTitle);
        mv.addObject(ParameterConstant.HTML_HEAD_NAME, htmlHeaderName);
        return mv;
    }

    @PermissionScanIgnore(loginRequired = false)
    @RequestMapping("/logout.do")
    public ModelAndView logout(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new ModelAndView("redirect:" + request.getContextPath() + "/login.do");
    }

    @PermissionScanIgnore
    @RequestMapping("/welcome.do")
    public ModelAndView welcome() {
        return new ModelAndView("welcome");
    }


    @ResponseBody
    @PermissionScanIgnore(loginRequired = false)
    @PostMapping("/getPublicKey.do")
    public Map<String, Object> getPublicKey() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", 0);
        map.put("msg", publicKey);
        return map;
    }

    /**
     * 生成验证码
     */
    @PermissionScanIgnore(loginRequired = false)
    @GetMapping(value = "/getVerify.do")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            //设置相应类型,告诉浏览器输出的内容为图片
            response.setContentType("image/jpeg");
            //设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            //输出验证码图片方法
            RandomValidateCodeUtil.getRandomCode(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
