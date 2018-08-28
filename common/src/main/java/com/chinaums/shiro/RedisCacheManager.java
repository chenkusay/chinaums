package com.chinaums.shiro;


import com.chinaums.redis.RedisUtil;
import com.chinaums.shiro.cache.RedisCache;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * RedisCacheManager存储实现类 TODO
 */
public class RedisCacheManager implements CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);

    @Autowired
    private RedisUtil redisUtil;

    // fast lookup by name map
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
    /**
     * The Redis key prefix for caches
     */
    private String keyPrefix = "shiro_redis_cache:";

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        logger.debug("获取名称为: " + name + " 的RedisCache实例");
        Cache c = caches.get(name);
        if (c == null){
            // create a new cache instance
            c = new RedisCache(keyPrefix + name);
            // add it to the cache collection
            caches.put(name, c);
        }
        return c;
    }
}
