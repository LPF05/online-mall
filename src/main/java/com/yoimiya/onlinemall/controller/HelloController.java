package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.entity.User;
import com.yoimiya.onlinemall.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Hello World 示例控制器
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    @Autowired
    private UserMapper userMapper;

    /**
     * Hello World 接口
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello World! MyBatis 框架搭建成功！";
    }

    /**
     * 测试 MyBatis 插入数据
     */
    @GetMapping("/test/insert")
    public String testInsert() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("123456");
        user.setEmail("test@example.com");
        user.setBalance(new BigDecimal("100.00"));
        userMapper.insert(user);
        return "插入成功！";
    }

    /**
     * 测试 MyBatis 查询数据
     */
    @GetMapping("/test/select")
    public List<User> testSelect() {
        return userMapper.findAll();
    }
}
