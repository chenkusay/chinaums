package com.chinaums.shiro.cache;

import com.chinaums.redis.RedisUtil;
import com.chinaums.utils.SerializeUtils;
import com.google.common.collect.Sets;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * redis缓存工具类 TODO
 */
public class RedisCache<K, V> implements Cache<K, V>{
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisUtil redisUtil;

    private String cacheKeyName = null;

    public RedisCache(String cacheKeyName) {
        this.cacheKeyName = cacheKeyName;
    }

    /**
     * 获取当前请求对象
     * @return
     */
    public static HttpServletRequest getRequest(){
        try{
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public V get(K k) throws CacheException {
        if (k == null){
            return null;
        }
        V v = null;
        HttpServletRequest request = this.getRequest();
        if (request != null){
            v = (V)request.getAttribute(cacheKeyName);
            if (v != null){
                return v;
            }
        }
        V value = null;
        byte[] bytes = redisUtil.get(cacheKeyName.getBytes());
        if(bytes != null && bytes.length > 0){
            value = (V) SerializeUtils.deSerialize(bytes);
        }
        logger.debug("get {} {} {}", cacheKeyName, k, request != null ? request.getRequestURI() : "");

        if (request != null && value != null){
            request.setAttribute(cacheKeyName, value);
        }
        return value;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        if (k == null){
            return null;
        }
        redisUtil.add(cacheKeyName.getBytes(), SerializeUtils.serialize(v));
        logger.debug("put {} {} = {}", cacheKeyName, k, v);

        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        V value = null;
        redisUtil.delete(cacheKeyName);
        logger.debug("remove {} {}", cacheKeyName, k);

        return value;
    }

    @Override
    public void clear() throws CacheException {
        redisUtil.delete(cacheKeyName);
        logger.debug("clear {}", cacheKeyName);
    }

    @Override
    public int size() {
        int size = 0;
        size = redisUtil.get(cacheKeyName.getBytes()).length;
        logger.debug("size {} {} ", cacheKeyName, size);
        return size;
    }

    @Override
    public Set<K> keys() {
        return Sets.newHashSet();
    }

    @Override
    public Collection<V> values() {
        return Collections.emptyList();
    }
}
