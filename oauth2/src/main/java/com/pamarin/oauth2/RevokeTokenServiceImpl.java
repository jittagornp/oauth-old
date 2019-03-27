/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.domain.OAuth2RefreshToken;
import com.pamarin.oauth2.repository.redis.RedisOAuth2AccessTokenRepo;
import com.pamarin.oauth2.repository.redis.RedisOAuth2RefreshTokenRepo;
import com.pamarin.oauth2.service.RevokeTokenService;
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

    @Autowired
    private RedisOAuth2AccessTokenRepo redisOAuth2AccessTokenRepo;

    @Autowired
    private RedisOAuth2RefreshTokenRepo redisOAuth2RefreshTokenRepo;

    @Autowired
    private MongoOperations mongoOps;

    private Query makeAttributeQuery(String attributeName, Object attributeValue) {
        return Query.query(Criteria.where(attributeName).is(attributeValue));
    }

    private void revokeTokenByAttribute(String attributeName, Object attributeValue) {
        Query query = makeAttributeQuery(attributeName, attributeValue);
        List<OAuth2AccessToken> accessTokens = mongoOps.find(query, OAuth2AccessToken.class);
        if (!isEmpty(accessTokens)) {
            accessTokens.forEach(accessToken -> {
                redisOAuth2AccessTokenRepo.deleteByTokenId(accessToken.getTokenId());
                redisOAuth2RefreshTokenRepo.deleteByTokenId(accessToken.getTokenId());
            });
        }
        mongoOps.remove(query, OAuth2AccessToken.class);
        mongoOps.remove(query, OAuth2RefreshToken.class);
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

}
