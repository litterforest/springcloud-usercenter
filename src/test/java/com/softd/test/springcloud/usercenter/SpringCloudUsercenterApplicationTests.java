package com.softd.test.springcloud.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SpringCloudUsercenterApplicationTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }

    /**
     * 初始化角色
     */
    @Test
    public void test1() {
        String insertStr = "insert into auth_role(role_code, role_name) values (?, ?)";
        List<Object[]> argList = new ArrayList<>();
        Object[] args = new Object[2];
        args[0] = "ROLE_ADMIN";
        args[1] = "普通管理员";
        argList.add(args);
        args = new Object[2];
        args[0] = "ROLE_SUPER_ADMIN";
        args[1] = "超级管理员";
        argList.add(args);
        args = new Object[2];
        args[0] = "ROLE_USER";
        args[1] = "普通用户";
        argList.add(args);
        int[] ints = jdbcTemplate.batchUpdate(insertStr, argList);
        System.out.println(ints);
    }

    /**
     * 初始化用户
     */
    @Test
    public void test2() {
        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder();
        String insertStr = "insert into auth_user(username, password, nick_name) values (?, ?, ?)";
        List<Object[]> argList = new ArrayList<>();
        Object[] args = new Object[3];
        args[0] = "zhangsan";
        args[1] = encoder.encode(args[0].toString());
        args[2] = "张三";
        argList.add(args);
        args = new Object[3];
        args[0] = "lisi";
        args[1] = encoder.encode(args[0].toString());
        args[2] = "李四";
        argList.add(args);
        args = new Object[3];
        args[0] = "admin";
        args[1] = encoder.encode(args[0].toString());
        args[2] = "管理员";
        argList.add(args);
        args = new Object[3];
        args[0] = "superAdmin";
        args[1] = encoder.encode(args[0].toString());
        args[2] = "超级管理员";
        argList.add(args);
        int[] ints = jdbcTemplate.batchUpdate(insertStr, argList);
        System.out.println(ints);
    }
}
