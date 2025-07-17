package com.study.toolsstudy.service;

import com.study.toolsstudy.dto.AuthResponse;
import com.study.toolsstudy.dto.LoginRequest;
import com.study.toolsstudy.dto.RegisterRequest;
import com.study.toolsstudy.entity.User;
import com.study.toolsstudy.repository.UserMapper;
import com.study.toolsstudy.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userMapper.selectByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        String token = jwtUtils.generateToken(user.getUsername(), user.getId(), user.getRole().name());
        return new AuthResponse(token, user.getUsername(), user.getRole().name(), user.getId());
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userMapper.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        // 检查邮箱是否已存在
        if (registerRequest.getEmail() != null && userMapper.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setNickname(registerRequest.getNickname());
        user.setRole(User.UserRole.USER);
        userMapper.insertUser(user);
        // 重新查一次，获取id
        User savedUser = userMapper.selectByUsername(user.getUsername());
        String token = jwtUtils.generateToken(savedUser.getUsername(), savedUser.getId(), savedUser.getRole().name());
        return new AuthResponse(token, savedUser.getUsername(), savedUser.getRole().name(), savedUser.getId());
    }

    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    public User getUserByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }
} 