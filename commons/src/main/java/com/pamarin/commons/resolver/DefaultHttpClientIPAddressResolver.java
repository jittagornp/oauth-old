/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Component
public class DefaultHttpClientIPAddressResolver implements HttpClientIPAddressResolver {

    private static final String[] IP_ADDRESS_HEADERS = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"
    };

    private static boolean hasIp(String ip) {
        if (!hasText(ip)) {
            return false;
        }

        return !"UNKNOWN".equalsIgnoreCase(ip);
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        if (httpReq == null) {
            throw new IllegalArgumentException("require httpReq.");
        }

        for (String header : IP_ADDRESS_HEADERS) {
            String ip = httpReq.getHeader(header);
            if (hasIp(ip)) {
                return ip;
            }
        }

        return httpReq.getRemoteAddr();
    }

}
