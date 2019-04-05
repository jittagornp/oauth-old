/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.commons.exception.AESEncryptionException;
import com.pamarin.commons.exception.InvalidHttpAuthorizationException;
import com.pamarin.commons.resolver.DefaultHttpCookieResolver;
import com.pamarin.commons.resolver.HttpCookieResolver;
import com.pamarin.commons.security.Base64AESEncryption;
import com.pamarin.commons.security.DefaultAESEncryption;
import com.pamarin.commons.security.DefaultBase64AESEncryption;
import com.pamarin.commons.util.CookieSpecBuilder;
import com.pamarin.commons.util.HttpAuthorizeBearerParser;
import com.pamarin.oauth2.constant.OAuth2Constant;
import com.pamarin.oauth2.collection.OAuth2AccessToken;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.service.AccessTokenVerification;
import java.util.Arrays;
import static java.util.Collections.emptyList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.session.web.http.CookieSerializer;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
public class SessionCookieSerializer implements CookieSerializer {

    private static final String RESOLVED = SessionCookieSerializer.class.getName() + "OAUTH2_RESOLVED_SESSION";

    private static final String ANONYMOUS = "anonymous";

    private final String cookieName;

    private int cookieMaxAge = -1;

    private boolean secure;

    private final Base64AESEncryption aesEncryption;

    private final String secretKey;

    private final HttpAuthorizeBearerParser httpAuthorizeBearerParser;

    private final AccessTokenVerification accessTokenVerification;

    private final HttpCookieResolver httpCookieResolver;

    public SessionCookieSerializer(String cookieName, String secretKey, HttpAuthorizeBearerParser httpAuthorizeBearerParser, AccessTokenVerification accessTokenVerification) {
        this.cookieName = cookieName;
        this.secretKey = secretKey;
        this.httpAuthorizeBearerParser = httpAuthorizeBearerParser;
        this.accessTokenVerification = accessTokenVerification;
        this.httpCookieResolver = new DefaultHttpCookieResolver(cookieName);
        this.aesEncryption = new DefaultBase64AESEncryption(DefaultAESEncryption.withKeyLength16());;
    }

    @Override
    public void writeCookieValue(CookieValue cookieValue) {
        int maxAge = -1;
        String token = ANONYMOUS;
        if (hasText(cookieValue.getCookieValue())) {
            maxAge = cookieMaxAge;
            token = aesEncryption.encrypt(cookieValue.getCookieValue(), secretKey);
        }

        cookieValue.getResponse().addHeader("Set-Cookie",
                new CookieSpecBuilder(cookieName, token)
                        .setHttpOnly(true)
                        .setSecure(secure)
                        .setPath("/")
                        .setMaxAge(maxAge)
                        .build()
        );
    }

    @Override
    public List<String> readCookieValues(HttpServletRequest request) {
        //protect double call on same request
        if (request.getAttribute(RESOLVED) == null) {
            request.setAttribute(RESOLVED, true);
            return resolve(request);
        } else {
            return emptyList();
        }
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    private List<String> resolve(HttpServletRequest httpReq) {
        String authorization = httpReq.getHeader("Authorization");
        if (!hasText(authorization)) {
            return resolveByCookie(httpReq);
        }

        if (isBearer(authorization)) {
            return resolveByHeader(httpReq, authorization);
        }

        return emptyList();
    }

    private boolean isBearer(String authorization) {
        Pattern pattern = Pattern.compile("^bearer\\s.*", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(authorization).matches();
    }

    public List<String> resolveByCookie(HttpServletRequest httpReq) {
        String value = httpCookieResolver.resolve(httpReq);
        if (!hasCookieValue(value)) {
            return emptyList();
        }

        try {
            return Arrays.asList(aesEncryption.decrypt(value, secretKey));
        } catch (AESEncryptionException ex) {
            return emptyList();
        }
    }

    private boolean hasCookieValue(String value) {
        return hasText(value) && !ANONYMOUS.equals(value);
    }

    private List<String> resolveByHeader(HttpServletRequest httpReq, String authorization) {
        try {
            String accessToken = httpAuthorizeBearerParser.parse(authorization);
            OAuth2AccessToken instance = accessTokenVerification.verify(accessToken);
            httpReq.setAttribute(OAuth2Constant.ACCESS_TOKEN_ATTRIBUTE, instance);
            return Arrays.asList(instance.getSessionId());
        } catch (InvalidHttpAuthorizationException | InvalidTokenException ex) {
            return emptyList();
        }
    }
}
