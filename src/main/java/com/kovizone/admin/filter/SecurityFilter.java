package com.kovizone.admin.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kovizone.admin.constant.HttpConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kovizone.admin.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * 安全过滤器
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-06 KoviChen 新建类
 * @version 0.0.1 2019-08-09 KoviChen 只用于日志输出请求地址和参数
 */
@Component
@WebFilter(filterName = "securityFilter", urlPatterns = "/*")
public class SecurityFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

	/**
	 * 静态资源
	 */
	private List<String> staticList = new ArrayList<>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		staticList.add(".html");
		staticList.add(".htm");
		staticList.add(".css");
		staticList.add(".js");
		staticList.add(".jpg");
		staticList.add(".png");
		staticList.add("/js/");
		staticList.add("/css/");
		staticList.add("/images/");
		staticList.add("/fonts/");
		staticList.add("druid");
		staticList.add("error");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpSession session = request.getSession(false);
		String sessionId = HttpUtils.getSessionSimpleId(session);
		boolean isAjax = HttpUtils.isAJAX(request);
		String ajaxFlag = isAjax ? "[" + HttpConstant.AJAX + "]" : "";

		String uri = request.getRequestURI();

		for (String staticResource : staticList) {
			if (uri.contains(staticResource)) {
				filterChain.doFilter(servletRequest, servletResponse);
				logger.debug(sessionId + ": " + uri + ajaxFlag + "[static]");
				return;
			}
		}
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterMap == null) {
			logger.info(sessionId + ": " + uri + ajaxFlag);
		} else {
			logger.info(sessionId + ": " + uri + ajaxFlag + ", ParameterMap: " + JSONObject.toJSONString(request.getParameterMap()));
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}
}
