package com.kovizone.admin.anno;

import java.lang.annotation.*;

/**
 * Shiro扫描权限忽略注解类<BR/>
 * 在控制层中对控制类或控制方法添加该注解，使其对应请求地址识别为无需授权的用户即可访问<BR/>
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-09 KoviChen 新建类
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionScanIgnore {

    /**
     * 需要登录
     *
     * @return 需要登录
     */
    boolean loginRequired() default true;
}
