package com.bookstore.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JsonMapper jsonMapper;

    public <T> T get(String key, Class<T> c) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o != null) {
            try {
                T t = jsonMapper.readValue(key, c);
                return t;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void set(String key, Object value) {
        try {
            String str = jsonMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, str);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(String key, Object value, Integer expireTime) {
        try {
            String str = jsonMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, str, expireTime, TimeUnit.MILLISECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void putHash(String key, String field, Object value) {
        try {
            String s = jsonMapper.writeValueAsString(value);
            redisTemplate.opsForHash().put(key, field, s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public <T> T getFromHash(String key, String field, Class<T> c) {
        Object o = redisTemplate.opsForHash().get(key, field);
        if (o != null) {
            T t = null;
            try {
                t = jsonMapper.readValue(o.toString(), c);
            } catch (JsonProcessingException e) {
                throw new RuntimeException();
            }
            return t;
        }
        return null;
    }

    public <T> List<T> getAllFromHash(String key, Class<T> c) {
        Map entries = redisTemplate.opsForHash().entries(key);
        List<T> list = new ArrayList<>();
        entries.forEach((k, v) -> {
            try {
                T t = jsonMapper.readValue(v.toString(), c);
                list.add(t);
            } catch (JsonProcessingException e) {
                throw new RuntimeException();
            }
        });
        return list;
    }

    public void delKey(String key) {
        redisTemplate.delete(key);
    }
}
