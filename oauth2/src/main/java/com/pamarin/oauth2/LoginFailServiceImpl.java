/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.generator.IdGenerator;
import com.pamarin.oauth2.collection.LoginFailHistory;
import com.pamarin.oauth2.service.LoginFailService;
import static java.time.LocalDateTime.now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

/**
 *
 * @author jitta
 */
@Service
public class LoginFailServiceImpl implements LoginFailService {

    private final IdGenerator idGenerator;

    private final MongoOperations mongoOperations;

    @Autowired
    public LoginFailServiceImpl(IdGenerator idGenerator, MongoOperations mongoOperations) {
        this.idGenerator = idGenerator;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void collect(String username) {
        LoginFailHistory history = LoginFailHistory.builder()
                .id(idGenerator.generate())
                .username(username)
                .failDate(now())
                .agentId(username)
                .ipAddress(username)
                .build();
        mongoOperations.save(history, LoginFailHistory.COLLECTION_NAME);
    }

    @Override
    public void verify(String username) {
    
    }

}
