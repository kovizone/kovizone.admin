package com.kovizone.admin.config;

import com.kovizone.admin.anno.PermissionScanIgnore;
import com.kovizone.admin.constant.ShiroFilterConstant;
import com.kovizone.admin.constant.UrlConstant;
import com.kovizone.admin.filter.PermsOnAccessDeniedFilter;
import com.kovizone.admin.mapper.SystemPermissionMapper;
import com.kovizone.admin.realm.CustomRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kovizone.admin.util.PackageUtil;

import javax.servlet.Filter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-08 KoviChen 新建类
 */
@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Value("${controller-scan}")
    private String controllerScan;

    private SystemPermissionMapper systemPermissionMapper;

    @Autowired(required = false)
    public void setSystemPermissionMapper(SystemPermissionMapper systemPermissionMapper) {
        this.systemPermissionMapper = systemPermissionMapper;
    }

    /**
     * 扫描控制层映射地址集（一个控制方法可能有多个请求地址）
     *
     * @param filterChainDefinitionMap 过滤映射Map
     * @param parentUrl                控制层父请求地址
     * @param mappings                 映射地址集
     * @param unrealizedUrlList        未实现地址权限
     * @param notRegisterUrlList       未注册地址权限
     */
    private void permissionScanMapping(Map<String, String> filterChainDefinitionMap, String parentUrl, String[] mappings,
                                       List<String> unrealizedUrlList, List<String> notRegisterUrlList) {
        for (String mapping : mappings) {
            if (mapping != null && !"".equals(mapping)) {
                String url = parentUrl + mapping;
                if (!unrealizedUrlList.contains(url)) {
                    notRegisterUrlList.add(url);
                } else {
                    unrealizedUrlList.remove(url);
                }
                filterChainDefinitionMap.put(url, ShiroFilterConstant.PERMS + "[" + url + "]");
            }
        }

    }

    /**
     * 扫描控制层方法
     *
     * @param filterChainDefinitionMap 过滤映射Map
     * @param parentUrl                控制层父请求地址
     * @param methods                  方法集
     * @param unrealizedUrlList        未实现地址权限
     * @param notRegisterUrlList       未注册地址权限
     */
    private void permissionScanMethod(Map<String, String> filterChainDefinitionMap, String parentUrl, Method[] methods,
                                      List<String> unrealizedUrlList, List<String> notRegisterUrlList) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(PermissionScanIgnore.class)) {
                continue;
            }
            String[] mappings = null;
            if (method.isAnnotationPresent(RequestMapping.class)) {
                mappings = method.getAnnotation(RequestMapping.class).value();
            } else if (method.isAnnotationPresent(PostMapping.class)) {
                mappings = method.getAnnotation(PostMapping.class).value();
            } else if (method.isAnnotationPresent(GetMapping.class)) {
                mappings = method.getAnnotation(GetMapping.class).value();
            }
            if (mappings != null) {
                permissionScanMapping(filterChainDefinitionMap, parentUrl, mappings, unrealizedUrlList, notRegisterUrlList);
            }
        }
    }

    /**
     * 扫描控制层
     *
     * @param filterChainDefinitionMap 过滤映射Map
     */
    private void permissionScan(Map<String, String> filterChainDefinitionMap) {
        // 获取已注册地址权限
        List<String> unrealizedUrlList = systemPermissionMapper.listUrl();
        List<String> notRegisterUrlList = new ArrayList<>();

        // 遍历控制层，生成权限（URL即为权限）
        List<String> classNames = PackageUtil.getClassName(controllerScan, true);
        if (classNames != null) {
            for (String className : classNames) {
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(PermissionScanIgnore.class)) {
                        continue;
                    }
                    String parentUrl = "";
                    if (clazz.isAnnotationPresent(RequestMapping.class)) {
                        parentUrl = clazz.getAnnotation(RequestMapping.class).value()[0];
                    }
                    permissionScanMethod(filterChainDefinitionMap, parentUrl, clazz.getMethods(), unrealizedUrlList, notRegisterUrlList);
                } catch (ClassNotFoundException e) {
                    logger.error(e.getMessage(), e);
                    return;
                }
            }
        }
        if (!unrealizedUrlList.isEmpty()) {
            logger.warn("待实现地址权限：" + unrealizedUrlList.toString());
        }
        if (!notRegisterUrlList.isEmpty()) {
            logger.warn("待注册地址权限：" + notRegisterUrlList.toString());
        }

    }

    /**
     * 生成权限过滤映射Map
     *
     * @return 权限过滤映射Map
     */
    private Map<String, String> filterChainDefinitionMap() {

        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 所有用户允许的地址
        filterChainDefinitionMap.put(UrlConstant.LOGIN_DO, ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put(UrlConstant.USER + UrlConstant.LOGIN_DO, ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put(UrlConstant.MENU_DO, ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put(UrlConstant.ERROR_DO, ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put(UrlConstant.GENERAL_DATA_DO, ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put(UrlConstant.SESSIONLESS_DO, ShiroFilterConstant.ANON);

        // Durid监控平台
        filterChainDefinitionMap.put("/druid/**", ShiroFilterConstant.ANON);

        // 静态资源
        filterChainDefinitionMap.put("/static/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/js/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/css/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/fonts/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/images/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/lib/**", ShiroFilterConstant.ANON);

        permissionScan(filterChainDefinitionMap);

        // 其他URL权限均为User
        filterChainDefinitionMap.put("/**", ShiroFilterConstant.USER);
        return filterChainDefinitionMap;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(UrlConstant.LOGIN_DO);
        shiroFilterFactoryBean.setUnauthorizedUrl(UrlConstant.ERROR_DO);

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put(ShiroFilterConstant.PERMS, new PermsOnAccessDeniedFilter());
        shiroFilterFactoryBean.setFilters(filters);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap());
        return shiroFilterFactoryBean;
    }

    /**
     * 注入 securityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        return securityManager;
    }

    /**
     * 自定义身份认证 realm;
     */
    @Bean
    public CustomRealm customRealm() {
        return new CustomRealm();
    }
}