/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.repository.mongodb;

import com.mongodb.WriteResult;
import com.pamarin.oauth2.domain.OAuth2Token;
import com.pamarin.oauth2.repository.OAuth2TokenRepositoryAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author jitta
 * @param <TOKEN>
 */
public abstract class MongodbOAuth2TokenRepositoryAdapter<TOKEN extends OAuth2Token> extends OAuth2TokenRepositoryAdapter<TOKEN> {

    private static final Logger LOG = LoggerFactory.getLogger(MongodbOAuth2TokenRepositoryAdapter.class);

    @Autowired
    private MongoOperations mongoOperations;

    private Query makeTokenIdQuery(String tokenId) {
        return Query.query(Criteria.where("tokenId").is(tokenId));
    }

    @Override
    public TOKEN doSave(TOKEN token) {
        LOG.debug("Mongodb save \"{}\" = {}", token.getId(), token);
        mongoOperations.save(token);
        return token;
    }

    @Override
    public TOKEN findByTokenId(String tokenId) {
        TOKEN token = mongoOperations.findOne(makeTokenIdQuery(tokenId), getTokenClass());
        LOG.debug("Mongodb findByTokenId \"{}\" = {}", tokenId, token);
        return token;
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        WriteResult result = mongoOperations.remove(makeTokenIdQuery(tokenId), getTokenClass());
        LOG.debug("Mongodb deleteByTokenId \"{}\" = {}", tokenId, result);
    }

}
