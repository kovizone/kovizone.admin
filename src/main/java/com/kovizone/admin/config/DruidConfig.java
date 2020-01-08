package com.kovizone.admin.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid监控平台配置
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-14 KoviChen 新建类
 */
@ConfigurationProperties(prefix = "druid.monitor")
@Configuration
public class DruidConfig {

	/**
	 * 访问地址
	 */
	private String url = "/druid/*";
	/**
	 * 监控平台登录账户
	 */
	private String loginUsername;
	/**
	 * 监控平台登录密码
	 */
	private String loginPassword;
	/**
	 * IP白名单，非空时仅允许该白名单IP访问监控平台
	 */
	private String allow;
	/**
	 * IP黑名单，非空时不允许该黑名单IP访问监控平台 若同时存在allow和deny，则deny优先级高于allow
	 */
	private String deny;
	/**
	 * 是否允许重置监控数据
	 */
	private String resetEnable;

	/**
	 * Servlet
	 *
	 * @return ServletRegistrationBean
	 */
	@Bean
	public ServletRegistrationBean<StatViewServlet> statViewServlet() {
		ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>(
				new StatViewServlet(), url);
		// 白名单
		servletRegistrationBean.addInitParameter("loginUsername", loginUsername);
		servletRegistrationBean.addInitParameter("loginPassword", loginPassword);
		servletRegistrationBean.addInitParameter("allow", allow);
		servletRegistrationBean.addInitParameter("deny", deny);
		servletRegistrationBean.addInitParameter("resetEnable", resetEnable);
		return servletRegistrationBean;
	}

	/**
	 * 过滤器
	 *
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean<WebStatFilter> statFilter() {
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(
				new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions",
				"*.html,.js,*.gif,*.jpg,*.png,*.css,*.json,*.ico,/druid/*");
		return filterRegistrationBean;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}

	public void setDeny(String deny) {
		this.deny = deny;
	}

	public void setResetEnable(String resetEnable) {
		this.resetEnable = resetEnable;
	}
}
