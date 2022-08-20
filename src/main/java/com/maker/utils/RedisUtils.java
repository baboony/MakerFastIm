package com.maker.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lucky winner
 */
@Service
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return TimeUnit.DAYS          //天
     * TimeUnit.HOURS         //小时
     * TimeUnit.MINUTES       //分钟
     * TimeUnit.SECONDS       //秒
     * TimeUnit.MILLISECONDS  //毫秒
     */
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的key
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    public void add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 判断{key}集合中是否存在{value}
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean isMember(String key, Object value) {
        SetOperations operations = redisTemplate.opsForSet();
        return operations.isMember(key, value);
    }

    public Long delMembers(String key, Object... value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.remove(key, value);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }
    

    public String lpop(String key) {
        Object value = null;
        try {
            ListOperations operations = redisTemplate.opsForList();
            value = operations.leftPop(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value == null ? null : value.toString();
    }

    public Long lpush(String key, String value) {
        Long leftPush = null;
        try {
            ListOperations operations = redisTemplate.opsForList();
            leftPush = operations.leftPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return leftPush;
    }

    public String rpop(String key) {
        Object value = null;

        try {
            ListOperations operations = redisTemplate.opsForList();
            value = operations.rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value == null ? null : value.toString();
    }

}
