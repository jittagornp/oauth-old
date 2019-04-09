/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Component
public class DefaultHostUrlProvider implements HostUrlProvider {

    private static final String DEFAULT_HOST_URL = "http://localhost:8080";

    private final String hostUrl;

    public DefaultHostUrlProvider(@Value("${server.hostUrl}") String hostUrl) {
        if (!hasText(hostUrl)) {
            hostUrl = DEFAULT_HOST_URL;
        }
        this.hostUrl = hostUrl;
    }

    @Override
    public String provide() {
        return hostUrl;
    }

}
