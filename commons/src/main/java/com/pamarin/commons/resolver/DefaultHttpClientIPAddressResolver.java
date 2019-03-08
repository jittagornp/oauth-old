/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.resolver;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Component
public class DefaultHttpClientIPAddressResolver implements HttpClientIPAddressResolver {

    private static final String[] IP_ADDRESS_HEADER = {
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

    private static boolean isValidFormat(String text) {
        Pattern pattern = Pattern.compile("^[\\d]+\\.[\\d]+\\.[\\d]+\\.[\\d]+$");
        return pattern.matcher(text).matches();
    }

    private static boolean hasIp(String ip) {
        if (!hasText(ip)) {
            return false;
        }

        return isValidFormat(ip);
    }

    @Override
    public String resolve(HttpServletRequest httpReq) {
        if (httpReq == null) {
            throw new IllegalArgumentException("require request.");
        }

        for (String header : IP_ADDRESS_HEADER) {
            String ip = httpReq.getHeader(header);
            if (hasIp(ip)) {
                return ip;
            }
        }

        return httpReq.getRemoteAddr();
    }

}
