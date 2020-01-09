package com.kovizone.admin.anno;

import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Import(PermissionScanRegistrar.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionScan {
    String[] value();
}
