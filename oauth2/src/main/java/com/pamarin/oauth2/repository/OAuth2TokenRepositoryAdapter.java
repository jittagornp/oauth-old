/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository;

import com.pamarin.commons.generator.UUIDGenerator;
import com.pamarin.oauth2.model.OAuth2Token;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import com.pamarin.commons.generator.IdGenerator;
import com.pamarin.commons.util.Base64Utils;
import org.springframework.beans.BeanUtils;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 * @param <T>
 */
public abstract class OAuth2TokenRepositoryAdapter<T extends OAuth2Token> implements OAuth2TokenRepository<T> {

    private static final int SECRET_KEY_SIZE = 7;

    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UUIDGenerator uuidGenerator;

    protected abstract Class<T> getTokenClass();

    protected abstract int getExpiresMinutes();

    protected abstract T doSave(T token);

    private void setIdIfNotPresent(T clone) {
        if (!hasText(clone.getId())) {
            clone.setId(idGenerator.generate());
        }
    }

    private void setTokenIdIfNotPresent(T clone) {
        if (!hasText(clone.getTokenId())) {
            clone.setTokenId(uuidGenerator.generate());
        }
    }

    private void setExpirationTimeIfNotPresent(T clone) {
        clone.setExpireMinutes(getExpiresMinutes());
        if (clone.getIssuedAt() < 1) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expires = now.plusMinutes(getExpiresMinutes());
            clone.setIssuedAt(Timestamp.valueOf(now).getTime());
            clone.setExpiresAt(Timestamp.valueOf(expires).getTime());
        }
    }

    private void setSecretKeyIfNotPresent(T clone) {
        if (!hasText(clone.getSecretKey())) {
            byte[] bytes = new byte[SECRET_KEY_SIZE];
            secureRandom.nextBytes(bytes);
            String secret = Base64Utils.encode(bytes);
            clone.setSecretKey(secret);
        }
    }

    @Override
    public T save(T token) {
        try {
            T clone = getTokenClass().newInstance();
            BeanUtils.copyProperties(token, clone);
            setIdIfNotPresent(clone);
            setTokenIdIfNotPresent(clone);
            setExpirationTimeIfNotPresent(clone);
            setSecretKeyIfNotPresent(clone);
            return doSave(clone);
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException("Can't save token", ex);
        }
    }

    protected T returnNullIfExpired(T token) {
        if (token == null) {
            return null;
        }
        long now = Timestamp.valueOf(LocalDateTime.now()).getTime();
        long expiresAt = token.getExpiresAt();
        if (expiresAt <= now) {
            deleteByTokenId(token.getTokenId());
            return null;
        }
        return token;
    }
}
