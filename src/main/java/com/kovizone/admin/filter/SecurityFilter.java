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

import com.alibaba.fastjson.JSONArray;
import com.kovizone.admin.constant.HttpConstant;
import com.kovizone.admin.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kovizone.admin.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * 安全过滤器
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-09 KoviChen 只用于日志输出请求地址和参数
 */
@Component
@WebFilter(filterName = "securityFilter", urlPatterns = "/*")
public class SecurityFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    /**
     * 静态资源
     */
    private List<String> staticResourcesTypeList = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        staticResourcesTypeList.add(".html");
        staticResourcesTypeList.add(".htm");
        staticResourcesTypeList.add(".css");
        staticResourcesTypeList.add(".js");
        staticResourcesTypeList.add(".jpg");
        staticResourcesTypeList.add(".png");
        staticResourcesTypeList.add(".ico");
        staticResourcesTypeList.add(".gif");
        staticResourcesTypeList.add(".woff2");
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
        int beginIndex = uri.lastIndexOf(".");
        int endIndex = (uri.contains("?") && uri.indexOf("?") > beginIndex) ? uri.indexOf("?") : uri.length();
        String type;
        if (beginIndex < endIndex && beginIndex >= 0) {
            type = uri.substring(beginIndex, endIndex);
        } else {
            type = null;
        }
        String method = request.getMethod();
        if (method.length() < 4) {
            method = " " + method;
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        String paramMsg = "";
        if (parameterMap != null && parameterMap.size() != 0) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(parameterMap));
            if (json.get("password") != null) {
                json.put("password", new JSONArray().fluentAdd(StringUtils.hide(String.valueOf(json.getJSONArray("password").get(0)))));
            }
            paramMsg = "ParameterMap:" + json;
        }
        String logStr = String.format("%s : %s -> %s %s %s", sessionId, method, uri, ajaxFlag, paramMsg);
        if (type != null) {
            for (String staticResourcesType : staticResourcesTypeList) {
                if (type.equals(staticResourcesType)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    logger.debug(logStr);
                    return;
                }
            }
        }
        logger.info(logStr);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
