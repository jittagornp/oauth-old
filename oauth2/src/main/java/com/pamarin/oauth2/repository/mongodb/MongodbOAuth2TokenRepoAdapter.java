/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.mongodb;

import com.pamarin.commons.generator.PrimaryKeyGenerator;
import com.pamarin.commons.generator.UUIDGenerator;
import com.pamarin.oauth2.domain.OAuth2Token;
import com.pamarin.oauth2.repository.OAuth2TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author jitta
 * @param <TOKEN>
 */
public abstract class MongodbOAuth2TokenRepoAdapter<TOKEN extends OAuth2Token> implements OAuth2TokenRepo<TOKEN> {

    @Autowired
    private MongoOperations mongoOps;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private PrimaryKeyGenerator primaryKeyGenerator;

    protected abstract Class<TOKEN> getTokenClass();

    private Query makeTokenIdQuery(String tokenId) {
        return Query.query(Criteria.where("tokenId").is(tokenId));
    }

    private void setIdIfNotPresent(TOKEN clone) {
        if (clone.getId() == null) {
            clone.setId(primaryKeyGenerator.generate());
        }
    }

    private void setTokenIdIfNotPresent(TOKEN clone) {
        if (clone.getTokenId() == null) {
            clone.setTokenId(uuidGenerator.generate());
        }
    }

    @Override
    public TOKEN save(TOKEN token) {
        setIdIfNotPresent(token);
        setTokenIdIfNotPresent(token);
        mongoOps.save(token);
        return token;
    }

    @Override
    public TOKEN findByTokenId(String tokenId) {
        return mongoOps.findOne(makeTokenIdQuery(tokenId), getTokenClass());
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        mongoOps.remove(makeTokenIdQuery(tokenId), getTokenClass());
    }

}
