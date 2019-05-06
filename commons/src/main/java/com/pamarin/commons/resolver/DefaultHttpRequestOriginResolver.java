/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import com.pamarin.commons.exception.InvalidURLException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Component
public class DefaultHttpRequestOriginResolver implements HttpRequestOriginResolver {

    private static final String CAHCED_KEY = HttpRequestOriginResolver.class.getName() + ".ORIGIN";

    @Override
    public URL resolve(HttpServletRequest httpReq) {
        Assert.notNull(httpReq, "require httpReq.");
        URL cached = (URL) httpReq.getAttribute(CAHCED_KEY);
        if (cached != null) {
            return cached;
        }

        String origin = httpReq.getHeader("Origin");
        if (!hasText(origin)) {
            origin = httpReq.getHeader("Referer");
        }
        try {
            return cached(new URL(origin), httpReq);
        } catch (MalformedURLException ex) {
            throw new InvalidURLException("Invalid url " + origin + ".");
        }
    }

    private URL cached(URL origin, HttpServletRequest httpReq) {
        httpReq.setAttribute(CAHCED_KEY, origin);
        return origin;
    }

}
