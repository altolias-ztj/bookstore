package com.bookstore.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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

}
