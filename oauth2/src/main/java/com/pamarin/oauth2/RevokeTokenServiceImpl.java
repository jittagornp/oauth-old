/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.collection.OAuth2RefreshToken;
import com.pamarin.oauth2.model.OAuth2Token;
import com.pamarin.oauth2.repository.redis.RedisOAuth2AccessTokenRepository;
import com.pamarin.oauth2.repository.redis.RedisOAuth2RefreshTokenRepository;
import com.pamarin.oauth2.service.RevokeTokenService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import static java.util.Collections.emptyList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 *
 * @author jitta
 */
public class RevokeTokenServiceImpl implements RevokeTokenService {

    private final RedisOAuth2AccessTokenRepository redisOAuth2AccessTokenRepository;

    private final RedisOAuth2RefreshTokenRepository redisOAuth2RefreshTokenRepository;

    private final MongoOperations mongoOperations;

    @Autowired
    public RevokeTokenServiceImpl(
            RedisOAuth2AccessTokenRepository redisOAuth2AccessTokenRepository,
            RedisOAuth2RefreshTokenRepository redisOAuth2RefreshTokenRepository,
            MongoOperations mongoOperations
    ) {
        this.redisOAuth2AccessTokenRepository = redisOAuth2AccessTokenRepository;
        this.redisOAuth2RefreshTokenRepository = redisOAuth2RefreshTokenRepository;
        this.mongoOperations = mongoOperations;
    }

    private Query makeAttributeQuery(String attributeName, Object attributeValue) {
        return Query.query(Criteria.where(attributeName).is(attributeValue));
    }

    private void revokeRedisToken(String tokenId) {
        redisOAuth2AccessTokenRepository.deleteByTokenId(tokenId);
        redisOAuth2RefreshTokenRepository.deleteByTokenId(tokenId);
    }

    private List<OAuth2AccessToken> findAccessTokens(Query query) {
        List<OAuth2AccessToken> accessTokens = mongoOperations.find(query, OAuth2AccessToken.class);
        if (isEmpty(accessTokens)) {
            return emptyList();
        }
        return accessTokens;
    }

    private void revokeTokenByAttribute(String attributeName, Object attributeValue) {
        Query query = makeAttributeQuery(attributeName, attributeValue);
        findAccessTokens(query).forEach(accessToken -> revokeRedisToken(accessToken.getTokenId()));
        mongoOperations.remove(query, OAuth2AccessToken.class);
        mongoOperations.remove(query, OAuth2RefreshToken.class);
    }

    @Override
    public void revokeByTokenId(String tokenId) {
        revokeTokenByAttribute("tokenId", tokenId);
    }

    @Override
    public void revokeBySessionId(String sessionId) {
        revokeTokenByAttribute("sessionId", sessionId);
    }

    @Override
    public void revokeByAgentId(String agentId) {
        revokeTokenByAttribute("agentId", agentId);
    }

    @Override
    public void revokeByUserId(String userId) {
        revokeTokenByAttribute("userId", userId);
    }

    @Override
    public void revokeByClientId(String clientId) {
        revokeTokenByAttribute("clientId", clientId);
    }

    @Override
    public void revokeExpiredTokens() {
        revokeExpiredTokens(OAuth2AccessToken.class);
        revokeExpiredTokens(OAuth2RefreshToken.class);
    }

    private <T extends OAuth2Token> void revokeExpiredTokens(Class<T> typeClass) {
        long nowTimestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
        Query query = Query.query(Criteria.where("expiresAt").lt(nowTimestamp));
        mongoOperations.remove(query, typeClass);
    }
}
