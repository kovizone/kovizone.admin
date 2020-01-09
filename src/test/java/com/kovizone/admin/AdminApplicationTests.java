package com.kovizone.admin;

import com.kovizone.admin.mapper.SystemPermissionMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.util.Set;

@SpringBootTest
class AdminApplicationTests {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() throws NoSuchFieldException, IllegalAccessException {
    }
}
