package com.kovizone.admin;

import com.kovizone.admin.anno.PermissionScan;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SpringBoot启动类
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-14 KoviChen 新建类
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.kovizone.admin.mapper")
@PermissionScan("com.kovizone.admin.controller.*")
public class AdminApplication {

    private static Logger logger = LoggerFactory.getLogger(AdminApplication.class);

    private static ConfigurableApplicationContext applicationContext;

    private static String[] args;

    public static void main(String[] args) {
        AdminApplication.applicationContext = SpringApplication.run(AdminApplication.class, args);
        AdminApplication.args = args;
    }

    public static void restart() {
        if (applicationContext != null) {
            applicationContext.close();
        }
        applicationContext = SpringApplication.run(AdminApplication.class, args);
    }
}