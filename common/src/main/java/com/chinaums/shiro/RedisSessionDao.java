package com.chinaums.shiro;


import com.chinaums.redis.RedisUtil;
import com.chinaums.utils.SerializeUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class RedisSessionDao extends EnterpriseCacheSessionDAO {
    private static final Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);

    @Autowired
    private RedisUtil redisUtil;

    /**
     * The Redis key prefix for caches
     */
//    private String keyPrefix = "shiro_redis_cache:";
    private String keyPrefix = "";

    /**
     * 创建session，保存到数据库
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        redisUtil.add(this.getByteKey(sessionId), SerializeUtils.serialize(session));
        return sessionId;
    }

    /**
     * 获取session
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        // 先从缓存中获取session，如果没有再去数据库中获取
        Session session = null;
//        if (session == null){
            byte[] bytes = redisUtil.get(this.getByteKey(sessionId));
            if(bytes != null && bytes.length > 0){
                session = (Session) SerializeUtils.deSerialize(bytes);
            }
//        }
        return session;
    }

    /**
     * 更新session的最后一次访问时间
     */
    @Override
    protected void doUpdate(Session session) {
//        session = doReadSession(session.getId());
        super.doUpdate(session);
//        logger.info("更新session的最后一次访问时间：{}", session.getId());
        redisUtil.add((this.getByteKey(session.getId())),  SerializeUtils.serialize(session));
    }
    /**
     * 删除session
     */
    @Override
    protected void doDelete(Session session) {
        super.doDelete(session);
        redisUtil.delete(session.getId().toString());
        redisUtil.delete(this.getByteKey(session.getId()));
    }

    /**
     * 获得byte[]型的key
     * @param sessionId
     * @return
     */
    private byte[] getByteKey(Serializable sessionId){
        String preKey = this.keyPrefix + sessionId;
        return preKey.getBytes();
    }
}
