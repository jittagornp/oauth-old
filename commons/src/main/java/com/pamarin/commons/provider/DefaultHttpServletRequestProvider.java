/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.provider;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Component
public class DefaultHttpServletRequestProvider implements HttpServletRequestProvider {

    @Override
    public HttpServletRequest provide() {
        return ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
    }

}
