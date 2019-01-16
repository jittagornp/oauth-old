/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 *
 * @author jitta
 */
@EnableRedisHttpSession
@Profile("!test") //inactive for test profile
public class RedisApplicationListener implements ApplicationListener<ApplicationEvent> {
    
    private static final Logger LOG = LoggerFactory.getLogger(RedisApplicationListener.class);

    @Value("${spring.session.timeout}")
    private Integer maxInactiveIntervalInSeconds;

    @Autowired
    private RedisOperationsSessionRepository redisOperation;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        
        if (event instanceof ContextRefreshedEvent) {
            LOG.debug("ContextRefreshedEvent..., redisOperation.setDefaultMaxInactiveInterval({})", maxInactiveIntervalInSeconds);
            redisOperation.setDefaultMaxInactiveInterval(maxInactiveIntervalInSeconds);
            return;
        }
        
        if(event instanceof HttpSessionCreatedEvent){
            HttpSessionCreatedEvent ev = (HttpSessionCreatedEvent)event;
            LOG.debug("session id {} was created...", ev.getSession().getId());
            return;
        }
        
        if(event instanceof HttpSessionDestroyedEvent){
            HttpSessionDestroyedEvent ev = (HttpSessionDestroyedEvent)event;
            LOG.debug("session id {} expires...", ev.getId());
        }
        
    }

}