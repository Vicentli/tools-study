package com.study.toolsstudy.controller;

import com.study.toolsstudy.common.Result;
import com.study.toolsstudy.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis测试控制器
 * 
 * @author study
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/redis")
@ConditionalOnClass(RedisUtils.class)
@ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "true", matchIfMissing = true)
public class RedisTestController {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 设置字符串缓存
     */
    @PostMapping("/set")
    public Result<String> set(@RequestParam String key, @RequestParam String value, 
                             @RequestParam(defaultValue = "0") long expire) {
        try {
            boolean success = redisUtils.set(key, value, expire);
            if (success) {
                return Result.success("设置成功", value);
            } else {
                return Result.error("设置失败");
            }
        } catch (Exception e) {
            log.error("设置缓存失败", e);
            return Result.error("设置缓存异常: " + e.getMessage());
        }
    }

    /**
     * 获取字符串缓存
     */
    @GetMapping("/get/{key}")
    public Result<Object> get(@PathVariable String key) {
        try {
            Object value = redisUtils.get(key);
            if (value != null) {
                return Result.success("获取成功", value);
            } else {
                return Result.notFound("缓存不存在");
            }
        } catch (Exception e) {
            log.error("获取缓存失败", e);
            return Result.error("获取缓存异常: " + e.getMessage());
        }
    }

    /**
     * 删除缓存
     */
    @DeleteMapping("/del/{key}")
    public Result<String> del(@PathVariable String key) {
        try {
            redisUtils.del(key);
            return Result.success("删除成功", key);
        } catch (Exception e) {
            log.error("删除缓存失败", e);
            return Result.error("删除缓存异常: " + e.getMessage());
        }
    }

    /**
     * 检查key是否存在
     */
    @GetMapping("/exists/{key}")
    public Result<Boolean> exists(@PathVariable String key) {
        try {
            boolean exists = redisUtils.hasKey(key);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            log.error("检查key是否存在失败", e);
            return Result.error("检查失败: " + e.getMessage());
        }
    }

    /**
     * 设置Hash缓存
     */
    @PostMapping("/hset")
    public Result<String> hset(@RequestParam String key, @RequestParam String field, 
                              @RequestParam String value) {
        try {
            boolean success = redisUtils.hset(key, field, value);
            if (success) {
                return Result.success("设置Hash成功", value);
            } else {
                return Result.error("设置Hash失败");
            }
        } catch (Exception e) {
            log.error("设置Hash缓存失败", e);
            return Result.error("设置Hash缓存异常: " + e.getMessage());
        }
    }

    /**
     * 获取Hash缓存
     */
    @GetMapping("/hget/{key}/{field}")
    public Result<Object> hget(@PathVariable String key, @PathVariable String field) {
        try {
            Object value = redisUtils.hget(key, field);
            if (value != null) {
                return Result.success("获取Hash成功", value);
            } else {
                return Result.notFound("Hash字段不存在");
            }
        } catch (Exception e) {
            log.error("获取Hash缓存失败", e);
            return Result.error("获取Hash缓存异常: " + e.getMessage());
        }
    }

    /**
     * 获取所有Hash字段
     */
    @GetMapping("/hgetall/{key}")
    public Result<Map<Object, Object>> hgetAll(@PathVariable String key) {
        try {
            Map<Object, Object> map = redisUtils.hmget(key);
            return Result.success("获取所有Hash字段成功", map);
        } catch (Exception e) {
            log.error("获取所有Hash字段失败", e);
            return Result.error("获取所有Hash字段异常: " + e.getMessage());
        }
    }

    /**
     * 递增
     */
    @PostMapping("/incr/{key}")
    public Result<Long> incr(@PathVariable String key, @RequestParam(defaultValue = "1") long delta) {
        try {
            long result = redisUtils.incr(key, delta);
            return Result.success("递增成功", result);
        } catch (Exception e) {
            log.error("递增失败", e);
            return Result.error("递增异常: " + e.getMessage());
        }
    }

    /**
     * 递减
     */
    @PostMapping("/decr/{key}")
    public Result<Long> decr(@PathVariable String key, @RequestParam(defaultValue = "1") long delta) {
        try {
            long result = redisUtils.decr(key, delta);
            return Result.success("递减成功", result);
        } catch (Exception e) {
            log.error("递减失败", e);
            return Result.error("递减异常: " + e.getMessage());
        }
    }

    /**
     * 设置过期时间
     */
    @PostMapping("/expire/{key}")
    public Result<String> expire(@PathVariable String key, @RequestParam long seconds) {
        try {
            boolean success = redisUtils.expire(key, seconds);
            if (success) {
                return Result.success("设置过期时间成功", key);
            } else {
                return Result.error("设置过期时间失败");
            }
        } catch (Exception e) {
            log.error("设置过期时间失败", e);
            return Result.error("设置过期时间异常: " + e.getMessage());
        }
    }

    /**
     * 获取过期时间
     */
    @GetMapping("/ttl/{key}")
    public Result<Long> ttl(@PathVariable String key) {
        try {
            long ttl = redisUtils.getExpire(key);
            return Result.success("获取过期时间成功", ttl);
        } catch (Exception e) {
            log.error("获取过期时间失败", e);
            return Result.error("获取过期时间异常: " + e.getMessage());
        }
    }
} 