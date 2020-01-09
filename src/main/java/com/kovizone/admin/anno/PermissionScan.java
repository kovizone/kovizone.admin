package com.kovizone.admin.anno;

import com.kovizone.admin.registrar.PermissionScanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限扫描
 *
 * @author KoviChen
 * @version 0.0.1 20200109 KoviChen 新建类
 */
@Target({ElementType.TYPE})
@Import(PermissionScanRegistrar.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionScan {
    String[] value();
}
