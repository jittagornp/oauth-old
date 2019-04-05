/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.model;

import com.pamarin.commons.util.QuerystringBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.bind.MissingServletRequestParameterException;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/24
 */
@Getter
@Setter
@Builder
public class AuthorizationRequest implements ClientDetails {

    private String responseType;
    private String clientId;
    private String redirectUri;
    private String scope;
    private String state;

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getRedirectUri() {
        return redirectUri;
    }

    public boolean isValidRequest() {
        return hasText(responseType)
                && hasText(clientId)
                && hasText(redirectUri);
    }

    public boolean haveSomeParameters() {
        return hasText(responseType)
                || hasText(clientId)
                || hasText(redirectUri)
                || hasText(scope)
                || hasText(state);
    }
    
    public void requireParameters() throws MissingServletRequestParameterException{
        if (!hasText(responseType)) {
            throw new MissingServletRequestParameterException("response_type", "String");
        }

        if (!hasText(clientId)) {
            throw new MissingServletRequestParameterException("client_id", "String");
        }

        if (!hasText(redirectUri)) {
            throw new MissingServletRequestParameterException("redirect_uri", "String");
        }
    }

    public void validateParameters() throws MissingServletRequestParameterException {
        if (!haveSomeParameters()) {
            return;
        }

        this.requireParameters();
    }

    public boolean responseTypeIsCode() {
        return "code".equals(responseType);
    }

    public String buildQuerystring() {
        return new QuerystringBuilder()
                .addParameter("response_type", responseType)
                .addParameter("client_id", clientId)
                .addParameter("redirect_uri", redirectUri)
                .addParameter("scope", scope)
                .addParameter("state", state)
                .build();
    }

    @Override
    public String getClientSecret() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getScopes() {
        if (!hasText(getScope())) {
            return Collections.emptyList();
        }
        return Arrays.asList(StringUtils.split(getScope(), ","));
    }
}
