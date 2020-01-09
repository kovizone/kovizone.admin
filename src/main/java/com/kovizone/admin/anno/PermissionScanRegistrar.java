package com.kovizone.admin.anno;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * 权限扫描注册器
 *
 * @author KoviChen
 * @version 0.0.1 20200109 KoviChen 新建类
 */
public class PermissionScanRegistrar implements ImportBeanDefinitionRegistrar {

    private static String[] permissionScans = null;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        String permissionClassName = PermissionScan.class.getName();
        if (importingClassMetadata.hasAnnotation(permissionClassName)) {
            Map<String, Object> permissionScanAnnotationAttributes =
                    importingClassMetadata.getAnnotationAttributes(permissionClassName);
            if (permissionScanAnnotationAttributes != null) {
                permissionScans = (String[]) permissionScanAnnotationAttributes.get("value");
            }
        }
    }

    public static String[] getPermissionScans() {
        return permissionScans;
    }
}
