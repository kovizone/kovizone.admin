package com.kovizone.admin.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kovizone.admin.constant.LayuiIconConstatnt;
import com.kovizone.admin.constant.ParameterConstant;
import com.kovizone.admin.constant.ViewConstant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kovizone.admin.anno.PermissionScanIgnore;
import com.kovizone.admin.bo.GeneralData;
import com.kovizone.admin.bo.Menu;
import com.kovizone.admin.bo.TableData;
import com.kovizone.admin.constant.MessageConstant;
import com.kovizone.admin.constant.UrlConstant;
import com.kovizone.admin.po.SystemPermission;
import com.kovizone.admin.service.SystemPermissionService;
import com.kovizone.admin.service.SystemUserService;
import com.kovizone.admin.util.DataUtils;

/**
 * 系统底层控制<BR/>
 * 仅一级请求地址
 * <P/>
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

	private static final String UPDATE_PERMISSION = "updatePermission";

	private SystemPermissionService systemPermissionService;

	private SystemUserService systemUserService;

	@Autowired
	public SystemController(SystemPermissionService systemPermissionService, SystemUserService systemUserService) {
		this.systemPermissionService = systemPermissionService;
		this.systemUserService = systemUserService;
	}

	@PermissionScanIgnore
	@RequestMapping(UrlConstant.GENERAL_DATA_DO)
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
	@RequestMapping(UrlConstant.INDEX_DO)
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView(ViewConstant.INDEX);
		Subject subject = SecurityUtils.getSubject();
		mv.addObject("testMode", "true");
		mv.addObject("systemUser", systemUserService.getByUname(String.valueOf(subject.getPrincipal())));
		mv.addObject("maxInactiveInterval", request.getSession(false).getMaxInactiveInterval());
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
	@PermissionScanIgnore
	@RequestMapping(UrlConstant.ERROR_DO)
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

		ModelAndView mv = new ModelAndView(ViewConstant.ERROR);
		mv.addObject(ParameterConstant.MESSAGE, message);
		mv.addObject(ParameterConstant.ICON, icon);
		return mv;
	}

	@PermissionScanIgnore
	@RequestMapping(UrlConstant.MENU_DO)
	@ResponseBody
	public Menu menu(HttpServletRequest request) {

		String type = request.getParameter(ParameterConstant.TYPE);
		List<SystemPermission> systemPermissions = null;

		if (type == null || "".equals(type)) {
			Subject subject = SecurityUtils.getSubject();
			systemPermissions = systemPermissionService.listContainParentByUname(String.valueOf(subject.getPrincipal()));
		}

		if (UPDATE_PERMISSION.equals(type)) {
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
		return menu;
	}

	@PermissionScanIgnore
	@RequestMapping(UrlConstant.LOGIN_DO)
	public ModelAndView login(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView(ViewConstant.LOGIN);
		mv.addObject(ParameterConstant.MESSAGE, request.getParameter("message"));
		return mv;
	}

	@PermissionScanIgnore
	@RequestMapping(UrlConstant.LOGOUT_DO)
	public ModelAndView logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return new ModelAndView("redirect:" + UrlConstant.LOGIN_DO);
	}

	@PermissionScanIgnore
	@RequestMapping(UrlConstant.WELCOME_DO)
	public ModelAndView welcome() {
		return new ModelAndView(ViewConstant.WELCOME_VIEW);
	}
}