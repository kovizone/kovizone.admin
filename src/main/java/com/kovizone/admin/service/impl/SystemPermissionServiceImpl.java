package com.kovizone.admin.service.impl;

import com.kovizone.admin.constant.CodeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kovizone.admin.bo.Menu;
import com.kovizone.admin.bo.MenuNode;
import com.kovizone.admin.bo.TableData;
import com.kovizone.admin.mapper.SystemPermissionMapper;
import com.kovizone.admin.mapper.SystemRolePermissionMapper;
import com.kovizone.admin.po.SystemPermission;
import com.kovizone.admin.service.SystemPermissionService;

import java.util.List;

/**
 * 系统权限服务实现类
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-12 KoviChen 新建类
 */
@Service
public class SystemPermissionServiceImpl implements SystemPermissionService {

	private SystemPermissionMapper systemPermissionMapper;

	private SystemRolePermissionMapper systemRolePermissionMapper;

	@Autowired(required = false)
	public SystemPermissionServiceImpl(SystemPermissionMapper systemPermissionMapper, SystemRolePermissionMapper systemRolePermissionMapper) {
		this.systemPermissionMapper = systemPermissionMapper;
		this.systemRolePermissionMapper = systemRolePermissionMapper;
	}

	/**
	 * 生成菜单
	 *
	 * @param systemPermissionList 方法数据集合
	 * @param parentno             父编号
	 * @return 方法数据集
	 */
	private Menu menu(List<SystemPermission> systemPermissionList, Integer parentno) {
		if (systemPermissionList == null || systemPermissionList.isEmpty()) {
			return null;
		}

		Menu menu = new Menu();

		// 遍历
		for (SystemPermission systemPermission : systemPermissionList) {
			Integer iteratorParentno = systemPermission.getParentno();
			boolean canBuild = false;

			// 该行数据的parentno为空，且形参（parentno）也为空
			if (parentno == null && iteratorParentno == null) {
				canBuild = true;
			}
			// 该行数据的parentno等于形参（parentno）
			if (!canBuild && iteratorParentno != null && iteratorParentno.equals(parentno)) {
				canBuild = true;
			}
			if (canBuild) {
				MenuNode menuNode = new MenuNode();
				menuNode.setId(systemPermission.getPno());
				menuNode.setName(systemPermission.getPname());
				menuNode.setOpen(parentno == null);
				menuNode.setChildren(menu(systemPermissionList, systemPermission.getPno()));
				menuNode.setShow("1".equals(systemPermission.getShow()));
				menuNode.setIcon(systemPermission.getIcon());
				menuNode.setTabUrl(systemPermission.getUrl());
				menuNode.setChecked(systemPermission.getParentno() == null);
				menu.add(menuNode);
			}
		}

		if (menu.isEmpty()) {
			return null;
		}

		return menu;
	}

	@Override
	public Menu buildMenu(List<SystemPermission> systemPermissionList) {
		return menu(systemPermissionList, null);
	}

	@Override
	public TableData<SystemPermission> tableData(SystemPermission systemPermission, int startNum, int size) {
		TableData<SystemPermission> list = new TableData<>();
		list.setData(systemPermissionMapper.list(systemPermission, startNum, size));
		list.setCount(systemPermissionMapper.count(systemPermission));
		return list;
	}

	@Override
	public int save(SystemPermission systemPermission) {
		return systemPermissionMapper.save(systemPermission);
	}

	@Override
	public int update(SystemPermission systemPermission) {
		Integer parentno = systemPermission.getParentno();
		Integer pno = systemPermission.getPno();
		List<SystemPermission> systemPermissions = systemPermissionMapper.listContainParentByPno(parentno);
		if (systemPermissions != null) {
			for (SystemPermission systemPermission1 : systemPermissions) {
				if (systemPermission1.getPno().equals(pno)) {
					return -1;
				}
			}
		}
		return systemPermissionMapper.update(systemPermission);
	}

	@Override
	public int remove(int pno) {
		SystemPermission systemPermission = new SystemPermission();
		systemPermission.setParentno(pno);
		List<SystemPermission> systemPermissions = systemPermissionMapper.list(systemPermission, 0, 0);
		if (systemPermissions != null && !systemPermissions.isEmpty()) {
			return CodeConstant.BYPASS_THE_IMMEDIATE_LEADERSHIP;
		}
		systemRolePermissionMapper.removeByPno(pno);
		return systemPermissionMapper.remove(pno);
	}

	@Override
	public List<SystemPermission> listContainParentByRno(int rno) {
		return systemPermissionMapper.listContainParentByRno(rno);
	}

	@Override
	public List<SystemPermission> listContainParentByPno(int pno) {
		return systemPermissionMapper.listContainParentByPno(pno);
	}

	@Override
	public List<SystemPermission> listContainParentByUno(int rno) {
		return systemPermissionMapper.listContainParentByRno(rno);
	}

	@Override
	public List<SystemPermission> listContainParentByUname(String uname) {
		return systemPermissionMapper.listContainParentByUname(uname);
	}

}
