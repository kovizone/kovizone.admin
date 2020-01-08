package com.kovizone.admin.po;

import java.util.Date;

/**
 * 系统用户PO
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-06 KoviChen 新建类
 */
public class SystemUser {

	private Integer uno;
	private String uname;
	private String password;
	private String salt;
	private String realname;
	private Date birthday;
	private String phone;
	private String status;
	private Integer createrno;

	private String creatername;
	private String rnos;
	private String rnames;

    @Override
    public String toString() {
        return "SystemUser{" +
                "uno=" + uno +
                ", uname='" + uname + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", realname='" + realname + '\'' +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", createrno=" + createrno +
                ", creatername='" + creatername + '\'' +
                ", rnos='" + rnos + '\'' +
                ", rnames='" + rnames + '\'' +
                '}';
    }

    public String getRnos() {
		return rnos;
	}

	public void setRnos(String rnos) {
		this.rnos = rnos;
	}

	public String getRnames() {
		return rnames;
	}

	public void setRnames(String rnames) {
		this.rnames = rnames;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getUno() {
		return uno;
	}

	public void setUno(Integer uno) {
		this.uno = uno;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCreaterno() {
		return createrno;
	}

	public void setCreaterno(Integer createrno) {
		this.createrno = createrno;
	}

	public String getCreatername() {
		return creatername;
	}

	public void setCreatername(String creatername) {
		this.creatername = creatername;
	}

}
