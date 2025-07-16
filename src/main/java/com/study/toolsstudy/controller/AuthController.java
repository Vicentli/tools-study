package com.study.toolsstudy.controller;

import com.study.toolsstudy.common.Result;
import com.study.toolsstudy.dto.AuthResponse;
import com.study.toolsstudy.dto.LoginRequest;
import com.study.toolsstudy.dto.RegisterRequest;
import com.study.toolsstudy.entity.User;
import com.study.toolsstudy.service.AuthService;
import com.study.toolsstudy.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @PostMapping("/login")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public Result<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = authService.register(registerRequest);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/profile")
    public Result<User> getProfile(HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            if (token == null) {
                return Result.error("未提供认证令牌");
            }
            
            if (!jwtUtils.validateToken(token)) {
                return Result.error("认证令牌无效或已过期");
            }
            
            Long userId = jwtUtils.extractUserId(token);
            User user = authService.getUserById(userId);
            
            // 不返回密码
            user.setPassword(null);
            
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/logout")
    public Result<String> logout() {
        // 在实际应用中，你可能需要将token加入黑名单
        return Result.success("退出登录成功");
    }
    
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 