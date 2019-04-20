/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.commons.provider.HttpServletRequestProvider;
import com.pamarin.commons.resolver.HttpClientIPAddressResolver;
import com.pamarin.oauth2.session.RedisSessionRepositoryImpl;
import com.pamarin.oauth2.RevokeTokenServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
import org.springframework.session.data.redis.RedisFlushMode;
import com.pamarin.oauth2.service.RevokeTokenService;
import com.pamarin.oauth2.repository.DatabaseSessionRepository;
import com.pamarin.oauth2.repository.redis.RedisOAuth2AccessTokenRepository;
import com.pamarin.oauth2.repository.redis.RedisOAuth2RefreshTokenRepository;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import com.pamarin.oauth2.session.SessionRepositoryImpl;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Configuration
@Profile("!test") //inactive for test profile
public class RedisConfig extends SpringHttpSessionConfiguration {

    @Value("${spring.session.timeout:#{1800}}")
    private int sessionTimeout;

    @Value("${spring.session.redis.namespace}")
    private String namespace;

    @Bean
    public SessionRepository newSessionRepository(
            RedisConnectionFactory factory,
            MongoOperations mongoOperations,
            HttpServletRequestProvider httpServletRequestProvider,
            UserAgentTokenIdResolver userAgentTokenIdResolver,
            HttpClientIPAddressResolver httpClientIPAddressResolver
    ) {
        SessionRepositoryImpl repository =  new SessionRepositoryImpl(
                createDefaultTemplate(factory),
                mongoOperations,
                httpServletRequestProvider,
                userAgentTokenIdResolver,
                httpClientIPAddressResolver
        );
        repository.setMaxInactiveIntervalInSeconds(sessionTimeout);
        repository.setSessionNameSpace(namespace);
        repository.setSynchronizeTimeout(1000 * 30); //30 seconds
        return repository;
    }

    private static RedisTemplate<Object, Object> createDefaultTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RevokeTokenService newRevokeTokenServiceImpl(
            RedisOAuth2AccessTokenRepository redisOAuth2AccessTokenRepository,
            RedisOAuth2RefreshTokenRepository redisOAuth2RefreshTokenRepository,
            MongoOperations mongoOperations
    ) {
        return new RevokeTokenServiceImpl(
                redisOAuth2AccessTokenRepository,
                redisOAuth2RefreshTokenRepository,
                mongoOperations
        );
    }
}
