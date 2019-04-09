/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.exception.InvalidSameOriginException;
import com.pamarin.commons.exception.InvalidURLException;
import com.pamarin.commons.resolver.DefaultHttpRequestOriginResolver;
import com.pamarin.commons.resolver.HttpRequestOriginResolver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jitta
 */
public class DefaultHttpRequestSameOriginVerifier implements HttpRequestSameOriginVerifier {

    private final URL origin;

    private final HttpRequestOriginResolver httpRequestOriginResolver;

    public DefaultHttpRequestSameOriginVerifier(String originUrl) {
        try {
            this.origin = new URL(originUrl);
        } catch (MalformedURLException ex) {
            throw new InvalidURLException(originUrl);
        }
        this.httpRequestOriginResolver = new DefaultHttpRequestOriginResolver();
    }

    private URL getURL(HttpServletRequest httpReq) {
        try {
            return httpRequestOriginResolver.resolve(httpReq);
        } catch (InvalidURLException ex) {
            throw new InvalidSameOriginException(ex.getMessage());
        }
    }

    @Override
    public void verify(HttpServletRequest httpReq) {
        URL requestURL = getURL(httpReq);
        if (!Objects.equals(origin.getProtocol(), requestURL.getProtocol())) {
            throw new InvalidSameOriginException("Invalid same origin, protocol.");
        }

        if (!Objects.equals(origin.getHost(), requestURL.getHost())) {
            throw new InvalidSameOriginException("Invalid same origin, host.");
        }

        if (!Objects.equals(origin.getPort(), requestURL.getPort())) {
            throw new InvalidSameOriginException("Invalid same origin, port.");
        }
    }

}
