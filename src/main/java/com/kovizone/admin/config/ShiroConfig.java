package com.kovizone.admin.config;

import com.kovizone.admin.anno.PermissionScanIgnore;
import com.kovizone.admin.anno.PermissionScanRegistrar;
import com.kovizone.admin.constant.ShiroFilterConstant;
import com.kovizone.admin.filter.PermissionsFilter;
import com.kovizone.admin.util.StringUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kovizone.admin.util.PackageUtil;

import javax.servlet.Filter;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Shiro配置
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-08 KoviChen 新建类
 */
@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    private AuthorizingRealm authorizingRealm;

    @Autowired(required = false)
    public ShiroConfig(AuthorizingRealm authorizingRealm) {
        this.authorizingRealm = authorizingRealm;
    }

    /**
     * 扫描控制层映射地址集（一个控制方法可能有多个请求地址）
     *
     * @param filterChainDefinitionMap 过滤映射Map
     * @param parentMappings           父映射请求集（控制层映射注解）
     * @param mappings                 映射地址集
     * @param filter                   权限过滤标识
     */
    private void permissionScanMapping(Map<String, String> filterChainDefinitionMap,
                                       String[] parentMappings,
                                       String[] mappings,
                                       String filter) {
        for (String parentUrl : parentMappings) {
            for (String mapping : mappings) {
                if (mapping != null && !"".equals(mapping)) {
                    String url = parentUrl + mapping;
                    // 处理RESTful的URL，替换为通配符
                    // 如：/{id}/{name}.do转换为/*/*.do
                    String newUrl = url.replaceAll("(\\{)[^{]*(})", "*");
                    // 若为RESTful的URL，则权限有两个
                    // 如：URL为：“/{id}/{name}.do”，权限则为：“/*/*.do和/{id}/{name}.do”
                    String perms = newUrl.equals(url) ? url : String.format("%s,%s", url, newUrl);
                    filterChainDefinitionMap.put(newUrl, filter.replace("#perms", perms));
                }
            }
        }
    }

    /**
     * 扫描控制层方法
     *
     * @param filterChainDefinitionMap 过滤映射Map
     * @param parentMappings           控制层父请求地址
     * @param methods                  方法集
     * @param filter                   权限过滤标识
     */
    private void permissionScanMethod(Map<String, String> filterChainDefinitionMap,
                                      String[] parentMappings,
                                      Method[] methods,
                                      String filter) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(PermissionScanIgnore.class)) {
                if (method.getAnnotation(PermissionScanIgnore.class).loginRequired()) {
                    continue;
                }
                filter = ShiroFilterConstant.ANON;
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
                permissionScanMapping(filterChainDefinitionMap, parentMappings, mappings, filter);
            }
        }
    }

    /**
     * 扫描控制层
     *
     * @param filterChainDefinitionMap 过滤映射Map
     */
    private void permissionScan(Map<String, String> filterChainDefinitionMap) {
        // 遍历控制层，生成权限（URL即为权限）
        String[] permissionScans = PermissionScanRegistrar.getPermissionScans();
        if (permissionScans == null) {
            throw new IllegalArgumentException("At least one base package must be specified");
        }
        for (String permissionScan : permissionScans) {
            List<String> classNames = PackageUtil.getClassName(permissionScan, false);
            if (classNames != null) {
                for (String className : classNames) {
                    try {
                        Class<?> clazz = Class.forName(className);
                        String filter = ShiroFilterConstant.PERMS + "[#perms]";
                        if (clazz.isAnnotationPresent(PermissionScanIgnore.class)) {
                            PermissionScanIgnore permissionScanIgnore = clazz.getAnnotation(PermissionScanIgnore.class);
                            if (permissionScanIgnore.loginRequired()) {
                                continue;
                            }
                            filter = ShiroFilterConstant.ANON;
                        }
                        String[] parentMappings;
                        if (clazz.isAnnotationPresent(RequestMapping.class)) {
                            parentMappings = clazz.getAnnotation(RequestMapping.class).value();
                        } else if (clazz.isAnnotationPresent(PostMapping.class)) {
                            parentMappings = clazz.getAnnotation(PostMapping.class).value();
                        } else if (clazz.isAnnotationPresent(GetMapping.class)) {
                            parentMappings = clazz.getAnnotation(GetMapping.class).value();
                        } else {
                            parentMappings = new String[]{""};
                        }
                        permissionScanMethod(filterChainDefinitionMap,
                                parentMappings,
                                clazz.getMethods(),
                                filter);

                    } catch (ClassNotFoundException e) {
                        logger.error(e.getMessage(), e);
                        return;
                    }
                }
            }
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

        // Durid监控平台
        filterChainDefinitionMap.put("/druid/**", ShiroFilterConstant.ANON);

        // 静态资源
        filterChainDefinitionMap.put("/static/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/js/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/css/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/fonts/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/images/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/lib/**", ShiroFilterConstant.ANON);
        filterChainDefinitionMap.put("/ico/**", ShiroFilterConstant.ANON);

        permissionScan(filterChainDefinitionMap);

        // 其他URL权限均为User
        filterChainDefinitionMap.put("/**", ShiroFilterConstant.USER);

        Set<Map.Entry<String, String>> entrySet = filterChainDefinitionMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();
            logger.debug(String.format("%s | %s", StringUtils.smartTab(key, 6), value));
        }
        return filterChainDefinitionMap;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login.do");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error.do");

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put(ShiroFilterConstant.PERMS, new PermissionsFilter());
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
        securityManager.setRealm(authorizingRealm);
        return securityManager;
    }
}
