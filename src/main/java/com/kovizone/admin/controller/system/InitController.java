package com.kovizone.admin.controller.system;

import com.kovizone.admin.anno.PermissionScanIgnore;
import com.kovizone.admin.bo.GeneralData;
import com.kovizone.admin.bo.TableData;
import com.kovizone.admin.constant.UrlConstant;
import com.kovizone.admin.po.SystemRole;
import com.kovizone.admin.service.SystemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 初始化控制
 * <P/>
 * URL字典
 * <TR>
 * <TD>/init/systemRoleList.do</TD>
 * <TD>获取系统角色集合</TD>
 * </TR>
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-15 KoviChen 新建类
 */
@PermissionScanIgnore
@RestController
@RequestMapping(UrlConstant.INIT)
public class InitController {

	private SystemRoleService systemRoleService;


	@Autowired
	public InitController(SystemRoleService systemRoleService) {
		this.systemRoleService = systemRoleService;
	}

	/**
	 * 获取系统角色集合
	 * 
	 * @return 通用数据
	 */
	@RequestMapping("/systemRoleList.do")
	public GeneralData systemRoleList() {
		TableData<SystemRole> systemRoleList = systemRoleService.tableData(null, 0, 0);
		GeneralData generalData = new GeneralData(true, "");
		generalData.setList(systemRoleList.getData());
		return generalData;
	}

}
