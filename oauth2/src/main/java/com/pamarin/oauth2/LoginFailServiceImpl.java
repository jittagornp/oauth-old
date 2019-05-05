/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.generator.IdGenerator;
import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.commons.util.CollectionUtils;
import static com.pamarin.commons.util.DateConverterUtils.convert2Timestamp;
import com.pamarin.oauth2.collection.LoginFailHistory;
import com.pamarin.oauth2.exception.LockUserException;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import com.pamarin.oauth2.service.LoginFailService;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.stereotype.Service;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 *
 * @author jitta
 */
@Service
public class LoginFailServiceImpl implements LoginFailService {

    private static final int EXPIRE_MINUTES = 15;

    private static final int NUMBER_OF_FAILS = 3;

    private static final int NUMBER_OF_IPS = 3;

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
        LocalDateTime now = now();
        long creationTime = convert2Timestamp(now);
        long expirationTime = convert2Timestamp(now.plusMinutes(EXPIRE_MINUTES));
        LoginFailHistory history = LoginFailHistory.builder()
                .id(idGenerator.generate())
                .username(username)
                .creationTime(creationTime)
                .expirationTime(expirationTime)
                .agentId(userAgentTokenIdResolver.resolve(httpReq))
                .ipAddress(httpClientIPAddressResolver.resolve(httpReq))
                .build();
        mongoOperations.save(history, LoginFailHistory.COLLECTION_NAME);
    }

    @Override
    public void verify(String username) {
        Query query = query(where("username").is(username));
        List<LoginFailHistory> histories = mongoOperations.find(query, LoginFailHistory.class);
        if (isEmpty(histories)) {
            return;
        }

        verify(histories);
    }

    public void verify(List<LoginFailHistory> histories) {
        String username = histories.get(0).getUsername();
        List<String> ips = histories.stream()
                .filter(history -> !history.isExpired())
                .map(history -> history.getIpAddress())
                .collect(toList());

        Map<String, Integer> duplicateMap = CollectionUtils.countDuplicateItems(ips);
        long failIps = duplicateMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= NUMBER_OF_FAILS)
                .count();

        if (failIps >= NUMBER_OF_IPS) {
            throw new LockUserException("lock user \"" + username + "\".");
        }

        String ipAddress = getIpAddress();
        Integer fails = duplicateMap.get(ipAddress);
        if (fails == null) {
            return;
        }

        if (failIps >= NUMBER_OF_FAILS) {
            throw new LockUserException("lock user \"" + username + "\", for ip address \"" + ipAddress + "\".");
        }
    }

    @Override
    public void clear(String username) {
        Query query = query(where("username").is(username).and("ipAddress").is(getIpAddress()));
        mongoOperations.remove(query, LoginFailHistory.class);
    }

    private String getIpAddress() {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        return httpClientIPAddressResolver.resolve(httpReq);
    }
}
