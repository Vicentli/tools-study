package com.study.toolsstudy.repository;

import com.study.toolsstudy.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    User selectById(@Param("id") Long id);
    User selectByUsername(@Param("username") String username);
    User selectByEmail(@Param("email") String email);
    int insertUser(User user);
    int updateUser(User user);
    int deleteById(@Param("id") Long id);
    boolean existsByUsername(@Param("username") String username);
    boolean existsByEmail(@Param("email") String email);
    List<User> selectAll();
} 