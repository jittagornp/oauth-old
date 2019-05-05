/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.generator.IdGenerator;
import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.oauth2.collection.LoginFailHistory;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import com.pamarin.oauth2.service.LoginFailService;
import static java.time.LocalDateTime.now;
import javax.servlet.http.HttpServletRequest;
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

    private final HttpServletRequestProvider httpServletRequestProvider;

    private final HttpClientIPAddressResolver httpClientIPAddressResolver;

    private final UserAgentTokenIdResolver userAgentTokenIdResolver;

    @Autowired
    public LoginFailServiceImpl(
            IdGenerator idGenerator,
            MongoOperations mongoOperations,
            HttpServletRequestProvider httpServletRequestProvider,
            HttpClientIPAddressResolver httpClientIPAddressResolver,
            UserAgentTokenIdResolver userAgentTokenIdResolver
    ) {
        this.idGenerator = idGenerator;
        this.mongoOperations = mongoOperations;
        this.httpServletRequestProvider = httpServletRequestProvider;
        this.httpClientIPAddressResolver = httpClientIPAddressResolver;
        this.userAgentTokenIdResolver = userAgentTokenIdResolver;
    }

    @Override
    public void collect(String username) {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        LoginFailHistory history = LoginFailHistory.builder()
                .id(idGenerator.generate())
                .username(username)
                .failDate(now())
                .agentId(userAgentTokenIdResolver.resolve(httpReq))
                .ipAddress(httpClientIPAddressResolver.resolve(httpReq))
                .build();
        mongoOperations.save(history, LoginFailHistory.COLLECTION_NAME);
    }

    @Override
    public void verify(String username) {

    }

}
