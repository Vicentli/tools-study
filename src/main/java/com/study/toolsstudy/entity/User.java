package com.study.toolsstudy.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private UserRole role;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public enum UserRole {
        USER, ADMIN
    }
} 