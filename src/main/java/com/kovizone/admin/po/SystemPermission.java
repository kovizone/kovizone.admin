package com.kovizone.admin.po;

/**
 * 系统权限PO
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-06 KoviChen 新建类
 */
public class SystemPermission {

	private Integer pno;
	private String pname;
	private String url;
	private String icon;
	private String show;
	private Integer parentno;

	/**
	 * 标记父级<br>
	 * 0-本级<br>
	 * 1-父级
	 */
	private Integer parent;

	public Integer getPno() {
		return pno;
	}

	public void setPno(Integer pno) {
		this.pno = pno;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public Integer getParentno() {
		return parentno;
	}

	public void setParentno(Integer parentno) {
		this.parentno = parentno;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}
}
