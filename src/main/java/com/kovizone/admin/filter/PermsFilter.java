package com.kovizone.admin.filter;

import com.kovizone.admin.constant.HttpConstant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kovizone.admin.constant.MessageConstant;
import com.kovizone.admin.util.HttpUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 权限认证失败过滤器
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-12 KoviChen 新建类
 */
public class PermsFilter extends PermissionsAuthorizationFilter {

    private Logger logger = LoggerFactory.getLogger(PermsFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String uri = httpServletRequest.getRequestURI();
        HttpSession session = httpServletRequest.getSession(false);
        String sessionId = HttpUtils.getSessionSimpleId(session);
        boolean isAjax = HttpUtils.isAJAX(request);
        String ajaxFlag = isAjax ? "[" + HttpConstant.AJAX + "]" : "";
        String errorMsg = session == null ? MessageConstant.SESSION_IS_EXPIRED : MessageConstant.PERMS_ERROR + "（" + uri + "）";

        if (session != null) {
            Subject subject = SecurityUtils.getSubject();
            if (subject == null || subject.getPrincipal() == null) {
                errorMsg = MessageConstant.USER_IS_EXPIRED;
            }
        }

        logger.info(sessionId + ": " + errorMsg + ajaxFlag);
        if (HttpUtils.isAJAX(request)) {
            String url = httpServletRequest.getContextPath();
            url += "/generalData.do";
            url += "?result=false";
            url += "&code=" + HttpConstant.FORBIDDEN;
            url += "&msg=" + URLEncoder.encode(errorMsg, "UTF-8");

            httpServletResponse.sendRedirect(url);
            return false;
        }
        super.onAccessDenied(request, response);
        return false;
    }
}