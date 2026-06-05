package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.common.Result;
import com.yoimiya.onlinemall.entity.User;
import com.yoimiya.onlinemall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public Result<User> getUserInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(userService.getUserInfo(userId));
    }

    @PostMapping("/recharge")
    public Result<Void> recharge(Authentication authentication, @RequestBody Map<String, BigDecimal> body) {
        Long userId = (Long) authentication.getPrincipal();
        userService.recharge(userId, body.get("amount"));
        return Result.success();
    }

    @PutMapping("/info")
    public Result<Void> updateUserInfo(Authentication authentication, @RequestBody Map<String, String> body) {
        Long userId = (Long) authentication.getPrincipal();
        userService.updateUserInfo(userId, body.get("username"), body.get("phone"));
        return Result.success();
    }

    @PostMapping("/change-password")
    public Result<Void> changePassword(Authentication authentication, @RequestBody Map<String, String> body) {
        Long userId = (Long) authentication.getPrincipal();
        userService.changePassword(userId, body.get("oldPassword"), body.get("newPassword"));
        return Result.success();
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }
}
