package com.chinaums.shiro;

import com.chinaums.utils.CollectionSerializer;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"rawtypes","unchecked"})
@Component("collectionRedisSessionDao")
@Lazy(false)
public class CollectionRedisSessionDao extends AbstractSessionDAO {
    private static final Logger logger = LoggerFactory.getLogger(CollectionRedisSessionDao.class);

    @Autowired
    private RedisTemplate redisTemplate;

    private String keyPrefix = "";

    @PostConstruct
    public void init() {
        logger.info("===》 Inject the collection sequence / anti sequence class");
        CollectionSerializer<Serializable> collectionSerializer = CollectionSerializer.getInstance();
        redisTemplate.setDefaultSerializer(collectionSerializer);
        //redisTemplate默认采用的其实是valueSerializer，就算是采用其他ops也一样，这是一个坑。
        redisTemplate.setValueSerializer(collectionSerializer);
    }

    /**
     * 创建session，保存到数据库
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        logger.debug("create seesion,id=[{}]", session.getId().toString());
        try {
            redisTemplate.opsForValue().set(session.getId().toString(), session,30,TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return sessionId;
    }

    /**
     * 获取session
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        logger.debug("read seesion,id=[{}]", sessionId.toString());
        Session readSession = null;
        try {
            readSession=(Session) redisTemplate.opsForValue().get(sessionId.toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return readSession;
    }


    @Override
    public void update(Session session) throws UnknownSessionException {
        logger.debug("update seesion,id=[{}]", session.getId().toString());
        try {
            redisTemplate.opsForValue().set(session.getId().toString(), session,30, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    @Override
    public void delete(Session session) {
        logger.debug("delete seesion,id=[{}]", session.getId().toString());
        try {
            redisTemplate.delete(session.getId().toString());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }
}
