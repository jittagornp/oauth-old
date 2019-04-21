/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

/**
 *
 * @author jitta
 */
public interface RevokeTokenService {

    void revokeByTokenId(String tokenId);

    void revokeBySessionId(String sessionId);

    void revokeByAgentId(String agentId);

    void revokeByUserId(String userId);

    void revokeByClientId(String clientId);

    void revokeExpiredTokens();
}
