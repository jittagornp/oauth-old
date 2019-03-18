package com.pamarin.oauth2;

import com.pamarin.oauth2.RedisSessionRepositoryImpl.RedisSession;
import com.pamarin.oauth2.service.DatabaseSessionSynchronizer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
public class RedisSessionRepositoryImpl implements FindByIndexNameSessionRepository<RedisSession>, MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(RedisSessionRepositoryImpl.class);

    @Autowired
    private DatabaseSessionSynchronizer databaseSessionSynchronizer;

    private static final String SESSION_KEY_PREFIX = "user-session:";

    private static final String CREATION_TIME_ATTR = "creationTime";

    private static final String MAX_INACTIVE_ATTR = "maxInactiveInterval";

    private static final String LAST_ACCESSED_ATTR = "lastAccessedTime";

    private static final String SESSION_ATTR_PREFIX = "sessionAttr:";

    private String keyPrefix = SESSION_KEY_PREFIX;

    private final RedisOperations<Object, Object> sessionRedisOperations;

    private Integer defaultMaxInactiveInterval;

    private RedisFlushMode redisFlushMode = RedisFlushMode.ON_SAVE;

    public RedisSessionRepositoryImpl(RedisConnectionFactory redisConnectionFactory) {
        this(createDefaultTemplate(redisConnectionFactory));
    }

    public RedisSessionRepositoryImpl(RedisOperations<Object, Object> sessionRedisOperations) {
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
        session.saveAttributeMap();
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
        LOG.debug("convertHashToMapSession({})...", sessionId);
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

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }

    public void setRedisKeyNamespace(String namespace) {
        this.keyPrefix = namespace + ":";
    }

    private String getSessionKey(String sessionId) {
        return this.keyPrefix + sessionId;
    }

    private BoundHashOperations<Object, Object, Object> getSessionBoundHashOperations(String sessionId) {
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
        private Map<String, Object> attributeMap = new HashMap<>();
        private boolean isNew;

        public RedisSession() {
            this(new MapSession());
            this.attributeMap.put(CREATION_TIME_ATTR, getCreationTime());
            this.attributeMap.put(MAX_INACTIVE_ATTR, getMaxInactiveIntervalInSeconds());
            this.attributeMap.put(LAST_ACCESSED_ATTR, getLastAccessedTime());
            this.isNew = true;
            this.flushImmediateIfNecessary();
        }

        public RedisSession(MapSession cached) {
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
            if (RedisSessionRepositoryImpl.this.redisFlushMode == RedisFlushMode.IMMEDIATE) {
                saveAttributeMap();
            }
        }

        private void putAndFlush(String attribute, Object value) {
            this.attributeMap.put(attribute, value);
            this.flushImmediateIfNecessary();
        }

        private void saveAttributeMap() {
            LOG.debug("saveAttributeMap()...");
            if (this.attributeMap.isEmpty()) {
                return;
            }

            databaseSessionSynchronizer.synchronize(this);

            String sessionId = getId();
            getSessionBoundHashOperations(sessionId).putAll(this.attributeMap);

            this.attributeMap = new HashMap<>(this.attributeMap.size());
        }
    }
}
