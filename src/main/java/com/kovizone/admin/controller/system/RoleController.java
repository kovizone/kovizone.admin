package com.kovizone.admin.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kovizone.admin.bo.GeneralData;
import com.kovizone.admin.bo.TableData;
import com.kovizone.admin.constant.CodeConstant;
import com.kovizone.admin.constant.MessageConstant;
import com.kovizone.admin.constant.UrlConstant;
import com.kovizone.admin.constant.ViewConstant;
import com.kovizone.admin.po.SystemRole;
import com.kovizone.admin.po.SystemRolePermission;
import com.kovizone.admin.service.SystemRolePermissionService;
import com.kovizone.admin.service.SystemRoleService;
import com.kovizone.admin.util.DataUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户角色控制
 * <P/>
 * URL字典
 * <TR>
 * <TD>/role/view.do</TD>
 * <TD>跳转到角色管理页</TD>
 * </TR>
 * <TR>
 * <TD>/role/save.do</TD>
 * <TD>新增/保存新角色</TD>
 * </TR>
 * <TR>
 * <TD>/role/update.do</TD>
 * <TD>更新/编辑角色</TD>
 * </TR>
 * <TR>
 * <TD>/role/list.do</TD>
 * <TD>获取角色数据集</TD>
 * </TR>
 * <TR>
 * <TD>/role/remove.do</TD>
 * <TD>删除角色</TD>
 * </TR>
 * <TR>
 * <TD>/role/setPermission.do</TD>
 * <TD>角色授权</TD>
 * </TR>
 * <TR>
 * <TD>/role/detail.do</TD>
 * <TD>角色权限详情</TD>
 * </TR>
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-06 KoviChen 新建类
 */
@RequestMapping(UrlConstant.ROLE)
@Controller
public class RoleController {

	private Logger logger = LoggerFactory.getLogger(RoleController.class);

	private SystemRolePermissionService systemRolePermissionService;

	private SystemRoleService systemRoleService;

	@Autowired
	public RoleController(SystemRolePermissionService systemRolePermissionService, SystemRoleService systemRoleService) {
		this.systemRolePermissionService = systemRolePermissionService;
		this.systemRoleService = systemRoleService;
	}

	@RequestMapping(UrlConstant.VIEW_DO)
	public ModelAndView view() {
		return new ModelAndView(ViewConstant.ROLE_VIEW);
	}

	@PostMapping(UrlConstant.UPDATE_DO)
	@ResponseBody
	public GeneralData update(HttpServletRequest request) {
		try {
			SystemRole systemRole = DataUtils.request2Object(request, SystemRole.class);
			int result = systemRoleService.update(systemRole);
			if (result > 0) {
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
	public TableData<SystemRole> list(HttpServletRequest request) {
		SystemRole systemRole = DataUtils.request2Object(request, SystemRole.class);
		return systemRoleService.tableData(systemRole, 0, 0);
	}

	@RequestMapping(UrlConstant.DETAIL_DO)
	@ResponseBody
	public GeneralData detail(HttpServletRequest request) {
		try {
			String rnoStr = request.getParameter("rno");
			if (rnoStr == null || "".equals(rnoStr)) {
				return new GeneralData(false, MessageConstant.QUERY_FAIL);
			}

			SystemRolePermission systemRolePermission = new SystemRolePermission();
			systemRolePermission.setRno(Integer.parseInt(rnoStr));
			List<SystemRolePermission> systemRolePermissions = systemRolePermissionService.list(systemRolePermission);
			StringBuilder pnoSb = new StringBuilder();
			if (systemRolePermissions != null && !systemRolePermissions.isEmpty()) {
				for (int i = 0; i < systemRolePermissions.size(); i++) {
					SystemRolePermission spr = systemRolePermissions.get(i);
					if (i != 0) {
						pnoSb.append(",");
					}
					pnoSb.append(spr.getPno());
				}
			}

			// SystemRole systemRole = systemRoleService.getByRno(Integer.parseInt(rnoStr));

			GeneralData generalData = new GeneralData(true, MessageConstant.QUERY_SUCCESS);
			generalData.addMap("pnoList", pnoSb.toString());
			return generalData;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new GeneralData(false, MessageConstant.EXCEPTION);
		}
	}

	@PostMapping(UrlConstant.SAVE_DO)
	@ResponseBody
	public GeneralData save(HttpServletRequest request) {
		SystemRole systemRole = DataUtils.request2Object(request, SystemRole.class);
		if (systemRoleService.save(systemRole) > 0) {
			return new GeneralData(true, MessageConstant.SAVE_SUCCESS);
		}
		return new GeneralData(false, MessageConstant.SAVE_FAIL);
	}

	@PostMapping("/setPermission.do")
	@ResponseBody
	public GeneralData setPermission(HttpServletRequest request) {
		String rnoStr = request.getParameter("rno");
		if (rnoStr == null || "".equals(rnoStr)) {
			return new GeneralData(false, MessageConstant.GRANT_FAIL);
		}
		String pnoListStr = request.getParameter("pnoList");
		Integer[] pnos = null;
		if (pnoListStr != null && !"".equals(pnoListStr)) {
			String[] pnoList = pnoListStr.split(",");
			if (pnoList.length > 0) {
				pnos = new Integer[pnoList.length];
				for (int i = 0; i < pnoList.length; i++) {
					pnos[i] = Integer.parseInt(pnoList[i]);
				}
			}
		}
		if (systemRolePermissionService.grant(Integer.parseInt(rnoStr), pnos) > 0) {
			return new GeneralData(true, MessageConstant.GRANT_SUCCESS);
		}
		return new GeneralData(false, MessageConstant.GRANT_FAIL);
	}

	@PostMapping(UrlConstant.REMOVE_DO)
	@ResponseBody
	public GeneralData remove(HttpServletRequest request) {
		try {
			int rno = Integer.parseInt(request.getParameter("rno"));
			int result = systemRoleService.remove(rno);
			if (result > 0) {
				return new GeneralData(false, MessageConstant.REMOVE_SUCCESS);
			}
			if (result == CodeConstant.ROLE_NOT_FOUND) {
				return new GeneralData(false, MessageConstant.ROLE_CLEAR);
			}
			return new GeneralData(false, MessageConstant.REMOVE_FAIL + "（" + result + "）");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new GeneralData(false, e.getMessage());
		}
	}
}
