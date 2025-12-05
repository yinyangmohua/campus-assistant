package top.seven.assistant.service;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /* 普通键值 */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    /* 哈希 */
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String field) {
        return (T) redisTemplate.opsForHash().get(key, field);
    }

    public Long getId(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /* 过期 */
    public Boolean expire(String key, long seconds) {
        return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }
}