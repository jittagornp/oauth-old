/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.config;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Configuration
@EnableRedisHttpSession
@Profile("!test") //inactive for test profile
public class RedisConf {
    
//    @NotBlank
//    @Value("${spring.redis.host}")
//    private String host; 
//    
//    @NotNull
//    @Value("${spring.redis.port}")
//    private Integer port;

//    @Bean
//    public RedisConnectionFactory jedisConnectionFactory() {
//        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
//        //jedisConFactory.setHostName(host);
//        //jedisConFactory.setPort(port);
//        jedisConFactory.se
//        return jedisConFactory;
//    }

}
