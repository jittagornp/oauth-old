/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.converter;

import com.pamarin.commons.resolver.UserAgent;
import com.pamarin.oauth2.domain.UserAgentEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class DefaultUserAgentConverter implements UserAgentConverter {

    @Override
    public UserAgentEntity convert(UserAgent userAgent) {
        UserAgentEntity entity = new UserAgentEntity();
        BeanUtils.copyProperties(userAgent, entity);
        return entity;
    }

}
