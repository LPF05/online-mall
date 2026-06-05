package com.yoimiya.onlinemall.service;

import com.yoimiya.onlinemall.dto.LoginRequest;
import com.yoimiya.onlinemall.dto.RegisterRequest;
import com.yoimiya.onlinemall.entity.User;
import com.yoimiya.onlinemall.exception.BusinessException;
import com.yoimiya.onlinemall.mapper.UserMapper;
import com.yoimiya.onlinemall.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void register(RegisterRequest req) {
        if (userMapper.findByEmail(req.getEmail()) != null) {
            throw new BusinessException("邮箱已被注册");
        }
        if (userMapper.findByUsername(req.getUsername()) != null) {
            throw new BusinessException("用户名已被使用");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail());
        user.setBalance(BigDecimal.ZERO);
        user.setRole("USER");
        userMapper.insert(user);
    }

    public String login(LoginRequest req) {
        User user = userMapper.findByEmail(req.getEmail());
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException("邮箱或密码错误");
        }
        return jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
    }

    public User getUserInfo(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null);
        return user;
    }

    public void recharge(Long userId, BigDecimal amount) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }
        userMapper.updateBalance(userId, user.getBalance().add(amount));
    }

    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    public void updateUserInfo(Long userId, String username, String phone) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (username != null && !username.trim().isEmpty()) {
            User existingUser = userMapper.findByUsername(username);
            if (existingUser != null && !existingUser.getId().equals(userId)) {
                throw new BusinessException("用户名已被使用");
            }
        }
        userMapper.updateUserInfo(userId, username, phone);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new BusinessException("新密码长度至少6位");
        }
        userMapper.updatePassword(userId, passwordEncoder.encode(newPassword));
    }
}