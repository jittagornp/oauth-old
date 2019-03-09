/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.pamarin.commons.exception.AESEncryptionException;
import com.pamarin.commons.exception.InvalidHttpAuthorizationException;
import com.pamarin.commons.security.Base64AESEncryption;
import com.pamarin.commons.security.DefaultAESEncryption;
import com.pamarin.commons.security.DefaultBase64AESEncryption;
import com.pamarin.commons.util.CookieSpecBuilder;
import com.pamarin.commons.util.HttpAuthorizeBearerParser;
import com.pamarin.oauth2.constant.OAuth2Constant;
import com.pamarin.oauth2.exception.InvalidTokenException;
import com.pamarin.oauth2.service.AccessTokenVerification;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.web.http.CookieSerializer;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
public class SessionCookieSerializer implements CookieSerializer {

    private static final String RESOLVE_SESSION_ATTRIBUTE = "OAUTH2_RESOLVE_SESSION";

    private static final String ANONYMOUS = "anonymous";

    private static final Logger LOG = LoggerFactory.getLogger(SessionCookieSerializer.class);

    private String cookieName = "user-session";

    private int cookieMaxAge = -1;

    private boolean secure;

    private final Base64AESEncryption aesEncryption = new DefaultBase64AESEncryption(DefaultAESEncryption.withKeyLength16());

    private final String secretKey;

    private final HttpAuthorizeBearerParser httpAuthorizeBearerParser;

    private final AccessTokenVerification accessTokenVerification;

    public SessionCookieSerializer(String secretKey, HttpAuthorizeBearerParser httpAuthorizeBearerParser, AccessTokenVerification accessTokenVerification) {
        this.secretKey = secretKey;
        this.httpAuthorizeBearerParser = httpAuthorizeBearerParser;
        this.accessTokenVerification = accessTokenVerification;
    }

    @Override
    public void writeCookieValue(CookieValue cookieValue) {
        String value = cookieValue.getCookieValue();
        boolean hasValue = hasText(value);
        int maxAge = hasValue ? cookieMaxAge : -1;
        String token = hasValue ? aesEncryption.encrypt(value, secretKey) : ANONYMOUS;
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
        //protect duplicate call on same request
        if (request.getAttribute(RESOLVE_SESSION_ATTRIBUTE) == null) {
            request.setAttribute(RESOLVE_SESSION_ATTRIBUTE, true);
            return resolve(request);
        } else {
            return Collections.emptyList();
        }
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    private List<String> resolve(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (hasText(authorization)) {
            if (isBearer(authorization)) {
                try {
                    String accessToken = httpAuthorizeBearerParser.parse(authorization);
                    AccessTokenVerification.Output output = accessTokenVerification.verify(accessToken);
                    request.setAttribute(OAuth2Constant.ACCESS_TOKEN_ATTRIBUTE, output);
                    return Arrays.asList(output.getSessionId());
                } catch (InvalidHttpAuthorizationException | InvalidTokenException ex) {
                    LOG.debug("error on resolve sessionId => {}", ex);
                    return Collections.emptyList();
                }
            }
            return Collections.emptyList();
        } else {
            return resolveByCookie(request);
        }
    }

    private boolean isBearer(String authorization) {
        Pattern pattern = Pattern.compile("^bearer\\s.*", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(authorization).matches();
    }

    public List<String> resolveByCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        List<String> values = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (this.cookieName.equals(cookie.getName())) {
                    if (hasValue(cookie.getValue())) {
                        try {
                            values.add(aesEncryption.decrypt(cookie.getValue(), secretKey));
                        } catch (AESEncryptionException ex) {
                            //swallow exception
                        }
                    }
                }
            }
        }
        return values;
    }

    private boolean hasValue(String value) {
        return hasText(value) && !ANONYMOUS.equals(value);
    }
}
