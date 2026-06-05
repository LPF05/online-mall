package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.common.Result;
import com.yoimiya.onlinemall.dto.LoginRequest;
import com.yoimiya.onlinemall.dto.RegisterRequest;
import com.yoimiya.onlinemall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody RegisterRequest req) {
        userService.register(req);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@Validated @RequestBody LoginRequest req) {
        String token = userService.login(req);
        return Result.success(Map.of("token", token));
    }
}
