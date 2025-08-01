package com.study.toolsstudy.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * 
 * @author study
 * @since 2024-01-01
 */
@Slf4j
@Component
@ConditionalOnClass(RedisTemplate.class)
@ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "true", matchIfMissing = true)
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${app.redis.key-prefix:tools:study:}")
    private String keyPrefix;

    /**
     * 生成带前缀的key
     */
    private String getKey(String key) {
        return keyPrefix + key;
    }

    // =============================common============================

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(getKey(key), time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("设置缓存失效时间失败，key: {}, time: {}", key, time, e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(getKey(key), TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(getKey(key)));
        } catch (Exception e) {
            log.error("判断key是否存在失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(getKey(key[0]));
            } else {
                Collection<String> keys = new java.util.ArrayList<>();
                for (String k : key) {
                    keys.add(getKey(k));
                }
                redisTemplate.delete(keys);
            }
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(getKey(key));
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(getKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败，key: {}, value: {}", key, value, e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(getKey(key), value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败，key: {}, value: {}, time: {}", key, value, time, e);
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(getKey(key), delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(getKey(key), -delta);
    }

    // ================================Map=================================

    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(getKey(key), item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(getKey(key));
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(getKey(key), map);
            return true;
        } catch (Exception e) {
            log.error("设置Hash缓存失败，key: {}, map: {}", key, map, e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(getKey(key), map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("设置Hash缓存失败，key: {}, map: {}, time: {}", key, map, time, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(getKey(key), item, value);
            return true;
        } catch (Exception e) {
            log.error("设置Hash缓存失败，key: {}, item: {}, value: {}", key, item, value, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(getKey(key), item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("设置Hash缓存失败，key: {}, item: {}, value: {}, time: {}", key, item, value, time, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(getKey(key), item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(getKey(key), item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(getKey(key), item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(getKey(key), item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(getKey(key));
        } catch (Exception e) {
            log.error("获取Set缓存失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(getKey(key), value));
        } catch (Exception e) {
            log.error("判断Set中是否存在值失败，key: {}, value: {}", key, value, e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(getKey(key), values);
        } catch (Exception e) {
            log.error("设置Set缓存失败，key: {}, values: {}", key, values, e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(getKey(key), values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("设置Set缓存失败，key: {}, time: {}, values: {}", key, time, values, e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(getKey(key));
        } catch (Exception e) {
            log.error("获取Set缓存长度失败，key: {}", key, e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(getKey(key), values);
            return count;
        } catch (Exception e) {
            log.error("移除Set缓存失败，key: {}, values: {}", key, values, e);
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1代表所有值
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(getKey(key), start, end);
        } catch (Exception e) {
            log.error("获取List缓存失败，key: {}, start: {}, end: {}", key, start, end, e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(getKey(key));
        } catch (Exception e) {
            log.error("获取List缓存长度失败，key: {}", key, e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(getKey(key), index);
        } catch (Exception e) {
            log.error("获取List缓存指定索引值失败，key: {}, index: {}", key, index, e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(getKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("设置List缓存失败，key: {}, value: {}", key, value, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(getKey(key), value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("设置List缓存失败，key: {}, value: {}, time: {}", key, value, time, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(getKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("设置List缓存失败，key: {}, value: {}", key, value, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(getKey(key), value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("设置List缓存失败，key: {}, value: {}, time: {}", key, value, time, e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(getKey(key), index, value);
            return true;
        } catch (Exception e) {
            log.error("更新List缓存指定索引值失败，key: {}, index: {}, value: {}", key, index, value, e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(getKey(key), count, value);
            return remove;
        } catch (Exception e) {
            log.error("移除List缓存失败，key: {}, count: {}, value: {}", key, count, value, e);
            return 0;
        }
    }
} 