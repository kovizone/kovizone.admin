package com.kovizone.admin.bo;

import java.util.List;

/**
 * 菜单节点类
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-14 KoviChen 新建类
 */
public class MenuNode {
	private Integer id;
	private String name;
	private boolean open;
	private boolean checked;
	private List<MenuNode> children;

	private boolean show;
	private String tabUrl;
	private String icon;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public List<MenuNode> getChildren() {
		return children;
	}

	public void setChildren(List<MenuNode> children) {
		this.children = children;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getTabUrl() {
		return tabUrl;
	}

	public void setTabUrl(String tabUrl) {
		this.tabUrl = tabUrl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
