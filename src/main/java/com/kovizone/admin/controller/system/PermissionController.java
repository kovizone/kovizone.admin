package com.kovizone.admin.controller.system;

import javax.servlet.http.HttpServletRequest;

import com.kovizone.admin.bo.GeneralData;
import com.kovizone.admin.bo.TableData;
import com.kovizone.admin.constant.CodeConstant;
import com.kovizone.admin.constant.MessageConstant;
import com.kovizone.admin.constant.ViewConstant;
import com.kovizone.admin.po.SystemPermission;
import com.kovizone.admin.po.SystemRole;
import com.kovizone.admin.service.SystemPermissionService;
import com.kovizone.admin.service.SystemRoleService;
import com.kovizone.admin.util.DataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kovizone.admin.constant.UrlConstant;

/**
 * 权限控制
 * <P/>
 * URL字典
 * <TR>
 * <TD>/permission/view.do</TD>
 * <TD>跳转到权限管理页</TD>
 * </TR>
 * <TR>
 * <TD>/permission/save.do</TD>
 * <TD>新增/保存新权限</TD>
 * </TR>
 * <TR>
 * <TD>/permission/update.do</TD>
 * <TD>更新/编辑权限</TD>
 * </TR>
 * <TR>
 * <TD>/permission/list.do</TD>
 * <TD>获取权限数据集</TD>
 * </TR>
 * <TR>
 * <TD>/permission/remove.do</TD>
 * <TD>删除权限</TD>
 * </TR>
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-06 新建类
 */
@RequestMapping(UrlConstant.PERMISSION)
@Controller
public class PermissionController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	private SystemPermissionService systemPermissionService;

	private SystemRoleService systemRoleService;

	@Autowired
	public PermissionController(SystemPermissionService systemPermissionService, SystemRoleService systemRoleService) {
		this.systemPermissionService = systemPermissionService;
		this.systemRoleService = systemRoleService;
	}

	@RequestMapping(UrlConstant.VIEW_DO)
	public ModelAndView view() {
		ModelAndView mv = new ModelAndView(ViewConstant.PERMISSION_VIEW);
		TableData<SystemRole> systemRoleList = systemRoleService.tableData(null, 0, 0);
		mv.addObject("systemRoleList", systemRoleList.getData());
		return mv;
	}

	@PostMapping(UrlConstant.SAVE_DO)
	@ResponseBody
	public GeneralData save(HttpServletRequest request) {
		SystemPermission systemPermission = DataUtils.request2Object(request, SystemPermission.class);
		if (systemPermissionService.save(systemPermission) > 0) {
			return new GeneralData(true, MessageConstant.SAVE_SUCCESS);
		}
		return new GeneralData(false, MessageConstant.SAVE_FAIL);
	}

	@PostMapping(UrlConstant.UPDATE_DO)
	@ResponseBody
	public GeneralData update(HttpServletRequest request) {
		try {
			SystemPermission systemPermission = DataUtils.request2Object(request, SystemPermission.class);

			if (systemPermissionService.update(systemPermission) > 0) {
				return new GeneralData(true, MessageConstant.UPDATE_SUCCESS);
			}
			return new GeneralData(false, MessageConstant.UPDATE_FAIL);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new GeneralData(false, MessageConstant.EXCEPTION);
		}
	}

	@RequestMapping(UrlConstant.LIST_DO)
	@ResponseBody
	public TableData<SystemPermission> list(HttpServletRequest request) {
		SystemPermission systemPermission = DataUtils.request2Object(request, SystemPermission.class);
		return systemPermissionService.tableData(systemPermission, 0, 0);
	}

	@PostMapping(UrlConstant.REMOVE_DO)
	@ResponseBody
	public GeneralData remove(HttpServletRequest request) {
		try {
			int pno = Integer.parseInt(request.getParameter("pno"));
			int result = systemPermissionService.remove(pno);
			if (result > 0) {
				return new GeneralData(true, MessageConstant.REMOVE_SUCCESS);
			}
			if (result == CodeConstant.BYPASS_THE_IMMEDIATE_LEADERSHIP) {
				return new GeneralData(false, MessageConstant.PERMISSION_CLEAR);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new GeneralData(false, e.getMessage());
		}
		return new GeneralData(false, MessageConstant.REMOVE_FAIL);
	}
}
