package com.pamarin.oauth2;

import com.pamarin.oauth2.RedisUserSessionRepository.RedisSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.util.Assert;

/**
 * @author Jitta
 */
public class RedisUserSessionRepository implements FindByIndexNameSessionRepository<RedisSession>, MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(RedisUserSessionRepository.class);

    static final String DEFAULT_SPRING_SESSION_REDIS_PREFIX = "user-session:";

    static final String CREATION_TIME_ATTR = "creationTime";

    static final String MAX_INACTIVE_ATTR = "maxInactiveInterval";

    static final String LAST_ACCESSED_ATTR = "lastAccessedTime";

    static final String SESSION_ATTR_PREFIX = "sessionAttr:";

    private String keyPrefix = DEFAULT_SPRING_SESSION_REDIS_PREFIX;

    private final RedisOperations<Object, Object> sessionRedisOperations;

    private Integer defaultMaxInactiveInterval;

    private RedisFlushMode redisFlushMode = RedisFlushMode.ON_SAVE;

    public RedisUserSessionRepository(RedisConnectionFactory redisConnectionFactory) {
        this(createDefaultTemplate(redisConnectionFactory));
    }

    public RedisUserSessionRepository(RedisOperations<Object, Object> sessionRedisOperations) {
        Assert.notNull(sessionRedisOperations, "sessionRedisOperations cannot be null");
        this.sessionRedisOperations = sessionRedisOperations;
    }


    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    public void setRedisFlushMode(RedisFlushMode redisFlushMode) {
        Assert.notNull(redisFlushMode, "redisFlushMode cannot be null");
        this.redisFlushMode = redisFlushMode;
    }

    @Override
    public void save(RedisSession session) {
        LOG.debug("save({})...", session.getId());

        session.saveDelta();
        if (session.isNew()) {
            session.setNew(false);
        }
    }

    @Override
    public RedisSession getSession(String id) {
        LOG.debug("getSession({})...", id);
        return getSession(id, false);
    }

    @Override
    public Map<String, RedisSession> findByIndexNameAndIndexValue(String indexName, String indexValue) {
        return Collections.emptyMap();
    }

    private RedisSession getSession(String sessionId, boolean allowExpired) {
        LOG.debug("getSession({}, {})...", sessionId, allowExpired);
        Map<Object, Object> hashEntries = getSessionBoundHashOperations(sessionId).entries();
        if (hashEntries.isEmpty()) {
            LOG.debug("entries.isEmpty()");
            return null;
        }
        MapSession mapSession = convertHashToMapSession(sessionId, hashEntries);
        if (!allowExpired && mapSession.isExpired()) {
            LOG.debug("!allowExpired && loaded.isExpired()");
            delete(sessionId);
            return null;
        }
        return new RedisSession(mapSession);
    }

    private MapSession convertHashToMapSession(String sessionId, Map<Object, Object> entries) {
        LOG.debug("loadSession...");
        MapSession mapSession = new MapSession(sessionId);
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            String key = (String) entry.getKey();
            if (CREATION_TIME_ATTR.equals(key)) {
                mapSession.setCreationTime((Long) entry.getValue());
            } else if (MAX_INACTIVE_ATTR.equals(key)) {
                mapSession.setMaxInactiveIntervalInSeconds((Integer) entry.getValue());
            } else if (LAST_ACCESSED_ATTR.equals(key)) {
                mapSession.setLastAccessedTime((Long) entry.getValue());
            } else if (key.startsWith(SESSION_ATTR_PREFIX)) {
                mapSession.setAttribute(key.substring(SESSION_ATTR_PREFIX.length()), entry.getValue());
            }
        }
        return mapSession;
    }

    @Override
    public void delete(String sessionId) {
        LOG.debug("delete({})...", sessionId);
        String key = getSessionKey(sessionId);
        this.sessionRedisOperations.delete(key);
    }

    @Override
    public RedisSession createSession() {
        LOG.debug("createSession()...");
        RedisSession redisSession = new RedisSession();
        if (this.defaultMaxInactiveInterval != null) {
            redisSession.setMaxInactiveIntervalInSeconds(this.defaultMaxInactiveInterval);
        }
        return redisSession;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onMessage(Message message, byte[] pattern) {

    }

    public void setRedisKeyNamespace(String namespace) {
        this.keyPrefix = DEFAULT_SPRING_SESSION_REDIS_PREFIX + namespace + ":";
    }

    String getSessionKey(String sessionId) {
        return this.keyPrefix + "sessions:" + sessionId;
    }

    private BoundHashOperations<Object, Object, Object> getSessionBoundHashOperations(
            String sessionId) {
        String key = getSessionKey(sessionId);
        return this.sessionRedisOperations.boundHashOps(key);
    }

    static String getSessionAttrNameKey(String attributeName) {
        return SESSION_ATTR_PREFIX + attributeName;
    }

    private static RedisTemplate<Object, Object> createDefaultTemplate(RedisConnectionFactory connectionFactory) {
        LOG.debug("createDefaultTemplate()...");
        Assert.notNull(connectionFactory, "connectionFactory cannot be null");
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    public class RedisSession implements ExpiringSession {

        private final MapSession cached;
        private Map<String, Object> delta = new HashMap<>();
        private boolean isNew;

        RedisSession() {
            this(new MapSession());
            this.delta.put(CREATION_TIME_ATTR, getCreationTime());
            this.delta.put(MAX_INACTIVE_ATTR, getMaxInactiveIntervalInSeconds());
            this.delta.put(LAST_ACCESSED_ATTR, getLastAccessedTime());
            this.isNew = true;
            this.flushImmediateIfNecessary();
        }

        RedisSession(MapSession cached) {
            Assert.notNull(cached, "MapSession cannot be null");
            this.cached = cached;
        }

        public void setNew(boolean isNew) {
            this.isNew = isNew;
            LOG.debug("setNew({})...", isNew);
        }

        @Override
        public void setLastAccessedTime(long lastAccessedTime) {
            LOG.debug("setLastAccessedTime({})...", lastAccessedTime);
            this.cached.setLastAccessedTime(lastAccessedTime);
            this.putAndFlush(LAST_ACCESSED_ATTR, getLastAccessedTime());
        }

        @Override
        public boolean isExpired() {
            return this.cached.isExpired();
        }

        public boolean isNew() {
            return this.isNew;
        }

        @Override
        public long getCreationTime() {
            return this.cached.getCreationTime();
        }

        @Override
        public String getId() {
            return this.cached.getId();
        }

        @Override
        public long getLastAccessedTime() {
            return this.cached.getLastAccessedTime();
        }

        @Override
        public void setMaxInactiveIntervalInSeconds(int interval) {
            this.cached.setMaxInactiveIntervalInSeconds(interval);
            this.putAndFlush(MAX_INACTIVE_ATTR, getMaxInactiveIntervalInSeconds());
        }

        @Override
        public int getMaxInactiveIntervalInSeconds() {
            return this.cached.getMaxInactiveIntervalInSeconds();
        }

        @Override
        public <T> T getAttribute(String attributeName) {
            return this.cached.getAttribute(attributeName);
        }

        @Override
        public Set<String> getAttributeNames() {
            return this.cached.getAttributeNames();
        }

        @Override
        public void setAttribute(String attributeName, Object attributeValue) {
            this.cached.setAttribute(attributeName, attributeValue);
            this.putAndFlush(getSessionAttrNameKey(attributeName), attributeValue);
        }

        @Override
        public void removeAttribute(String attributeName) {
            this.cached.removeAttribute(attributeName);
            this.putAndFlush(getSessionAttrNameKey(attributeName), null);
        }

        private void flushImmediateIfNecessary() {
            if (RedisUserSessionRepository.this.redisFlushMode == RedisFlushMode.IMMEDIATE) {
                saveDelta();
            }
        }

        private void putAndFlush(String a, Object v) {
            this.delta.put(a, v);
            this.flushImmediateIfNecessary();
        }

        private void saveDelta() {
            LOG.debug("saveDelta()...");
            if (this.delta.isEmpty()) {
                return;
            }
            String sessionId = getId();
            getSessionBoundHashOperations(sessionId).putAll(this.delta);
            this.delta = new HashMap<>(this.delta.size());
        }
    }
}
