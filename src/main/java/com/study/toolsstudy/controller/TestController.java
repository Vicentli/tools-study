package com.study.toolsstudy.controller;

import com.study.toolsstudy.common.Result;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 
 * @author study
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * Hello接口
     */
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello World! 当前时间：" + LocalDateTime.now());
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("服务运行正常！当前时间：" + LocalDateTime.now());
    }

    /**
     * 获取系统信息
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "tools-study");
        info.put("version", "1.0.0");
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("timestamp", LocalDateTime.now());
        info.put("status", "running");
        
        return Result.success(info);
    }

    /**
     * 测试POST接口
     */
    @PostMapping("/echo")
    public Result<Map<String, Object>> echo(@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "数据接收成功");
        response.put("receivedData", data);
        response.put("timestamp", LocalDateTime.now());
        
        return Result.success(response);
    }

    /**
     * 测试参数传递
     */
    @GetMapping("/param")
    public Result<Map<String, Object>> testParam(
            @RequestParam(defaultValue = "world") String name,
            @RequestParam(defaultValue = "18") Integer age) {
        
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("age", age);
        result.put("message", "Hello " + name + "! You are " + age + " years old.");
        result.put("timestamp", LocalDateTime.now());
        
        return Result.success(result);
    }
} 