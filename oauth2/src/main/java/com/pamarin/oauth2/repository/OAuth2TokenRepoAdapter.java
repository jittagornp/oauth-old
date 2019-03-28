/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.commons.generator.UUIDGenerator;
import com.pamarin.oauth2.domain.OAuth2Token;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import com.pamarin.commons.generator.IdGenerator;

/**
 *
 * @author jitta
 * @param <TOKEN>
 */
public abstract class OAuth2TokenRepoAdapter<TOKEN extends OAuth2Token> implements OAuth2TokenRepo<TOKEN> {

    private static final int SECRET_KEY_SIZE = 7;

    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UUIDGenerator uuidGenerator;

    protected abstract Class<TOKEN> getTokenClass();

    protected abstract int getExpiresMinutes();

    protected abstract TOKEN doSave(TOKEN token);

    private void setIdIfNotPresent(TOKEN clone) {
        if (clone.getId() == null) {
            clone.setId(idGenerator.generate());
        }
    }

    private void setTokenIdIfNotPresent(TOKEN clone) {
        if (clone.getTokenId() == null) {
            clone.setTokenId(uuidGenerator.generate());
        }
    }

    private void setExpirationTimeIfNotPresent(TOKEN clone) {
        clone.setExpireMinutes(getExpiresMinutes());
        if (clone.getIssuedAt() < 1) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expires = now.plusMinutes(getExpiresMinutes());
            clone.setIssuedAt(Timestamp.valueOf(now).getTime());
            clone.setExpiresAt(Timestamp.valueOf(expires).getTime());
        }
    }

    private void setSecretKeyIfNotPresent(TOKEN clone) {
        if (clone.getSecretKey() == null) {
            byte[] bytes = new byte[SECRET_KEY_SIZE];
            secureRandom.nextBytes(bytes);
            String secret = Base64.getEncoder().encodeToString(bytes);
            clone.setSecretKey(secret);
        }
    }

    @Override
    public TOKEN save(TOKEN token) {
        try {
            TOKEN clone = (TOKEN) token.clone();
            setIdIfNotPresent(clone);
            setTokenIdIfNotPresent(clone);
            setExpirationTimeIfNotPresent(clone);
            setSecretKeyIfNotPresent(clone);
            return doSave(clone);
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("Can't clone token", ex);
        }
    }
}
