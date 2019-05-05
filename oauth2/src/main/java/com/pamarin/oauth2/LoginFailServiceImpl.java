/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.commons.generator.IdGenerator;
import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import static com.pamarin.commons.util.CollectionUtils.countDuplicateItems;
import static com.pamarin.commons.util.DateConverterUtils.convert2Timestamp;
import com.pamarin.oauth2.collection.LoginFailHistory;
import com.pamarin.oauth2.exception.LockUserException;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import com.pamarin.oauth2.service.LoginFailService;
import static java.time.LocalDateTime.now;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
        long creationTime = convert2Timestamp(now());
        long expirationTime = creationTime + TimeUnit.MINUTES.toMillis(EXPIRE_MINUTES);
        mongoOperations.save(
                LoginFailHistory.builder()
                        .id(idGenerator.generate())
                        .username(username)
                        .creationTime(creationTime)
                        .expirationTime(expirationTime)
                        .agentId(userAgentTokenIdResolver.resolve(httpReq))
                        .ipAddress(httpClientIPAddressResolver.resolve(httpReq))
                        .build(),
                LoginFailHistory.COLLECTION_NAME
        );
    }

    @Override
    public void verify(String username) {
        Query query = query(where("username").is(username));
        verify(mongoOperations.find(query, LoginFailHistory.class));
    }

    public void verify(List<LoginFailHistory> histories) {
        if (isEmpty(histories)) {
            return;
        }
        String username = histories.get(0).getUsername();
        List<String> ips = aliveIpAddress(histories);

        Map<String, Integer> duplicateMap = countDuplicateItems(ips);
        if (countFailIps(duplicateMap) >= NUMBER_OF_IPS) {
            throw new LockUserException("lock user \"" + username + "\".");
        }

        String ipAddress = getRequestIpAddress();
        Integer count = duplicateMap.get(ipAddress);
        if (count != null && count >= NUMBER_OF_FAILS) {
            throw new LockUserException("lock user \"" + username + "\", for ip address \"" + ipAddress + "\".");
        }
    }

    private List<String> aliveIpAddress(List<LoginFailHistory> histories) {
        return histories.stream()
                .filter(history -> !history.isExpired())
                .map(history -> history.getIpAddress())
                .collect(toList());
    }

    private long countFailIps(Map<String, Integer> duplicateMap) {
        return duplicateMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= NUMBER_OF_FAILS)
                .count();
    }

    @Override
    public void clear(String username) {
        Query query = query(where("username").is(username).and("ipAddress").is(getRequestIpAddress()));
        mongoOperations.remove(query, LoginFailHistory.class);
    }

    private String getRequestIpAddress() {
        HttpServletRequest httpReq = httpServletRequestProvider.provide();
        return httpClientIPAddressResolver.resolve(httpReq);
    }
}
