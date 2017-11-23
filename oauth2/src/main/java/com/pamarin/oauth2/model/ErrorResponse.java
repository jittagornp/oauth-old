/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pamarin.commons.util.QuerystringBuilder;
import com.pamarin.commons.validator.ValidUri;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/26
 */
public class ErrorResponse {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorResponse.class);

    private final ValidUri.Validator validUriValidator = new ValidUri.Validator();

    private String error;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("error_description")
    private String errorDescription;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("error_uri")
    private String errorUri;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String state;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorUri() {
        return errorUri;
    }

    public void setErrorUri(String errorUri) {
        this.errorUri = errorUri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String toJSON() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public String buildQuerystring() {
        return new QuerystringBuilder()
                .addParameter("error", getError())
                .addParameter("error_description", getErrorDescription())
                .addParameter("error_uri", getErrorUri())
                .addParameter("state", getState())
                .build();
    }

    public static class Builder {

        private String error;

        private String errorDescription;

        private String errorUri;

        private String state;

        public Builder setError(String error) {
            this.error = error;
            return this;
        }

        public Builder setErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
            return this;
        }

        public Builder setErrorUri(String errorUri) {
            this.errorUri = errorUri;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public ErrorResponse build() {
            ErrorResponse resp = new ErrorResponse();
            resp.setError(error);
            resp.setErrorDescription(errorDescription);
            resp.setErrorUri(errorUri);
            resp.setState(state);
            return resp;
        }
    }

    public String makeRedirectUri(String redirectUri) {
        if (!hasText(redirectUri)) {
            throw new IllegalArgumentException("Required redirectUri.");
        }
        return redirectUri + (redirectUri.contains("?") ? "&" : "?")
                + buildQuerystring();
    }

    public void returnError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isProduceJSON(request)) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().print(this.toJSON());
        } else {
            String uri = request.getParameter("redirect_uri");
            if (hasText(uri) && validUriValidator.isValid(uri)) {
                response.sendRedirect(makeRedirectUri(uri));
            } else {
                response.setContentType(MediaType.TEXT_HTML_VALUE);
                response.getWriter().print(getError());
            }
        }
    }

    private boolean isProduceJSON(HttpServletRequest request) {
        return request.getRequestURI()
                .matches("/token");
    }

    /**
     * The request is missing a required parameter, includes an unsupported
     * parameter value (other than grant type), repeats a parameter, includes
     * multiple credentials, utilizes more than one mechanism for authenticating
     * the client, or is otherwise malformed.
     *
     * @return response
     */
    public static ErrorResponse invalidRequest() {
        return new Builder()
                .setError("invalid_request")
                .build();
    }

    /**
     * The resource owner or authorization server denied the request.
     *
     * @return response
     */
    public static ErrorResponse accessDenied() {
        return new Builder()
                .setError("access_denied")
                .build();
    }

    /**
     * The authorization server does not support obtaining an authorization code
     * using this method.
     *
     * @return response
     */
    public static ErrorResponse unsupportedResponseType() {
        return new Builder()
                .setError("unsupported_response_type")
                .build();
    }

    /**
     * The authorization server encountered an unexpected condition that
     * prevented it from fulfilling the request. (This error code is needed
     * because a 500 Internal Server Error HTTP status code cannot be returned
     * to the client via an HTTP redirect.)
     *
     * @return response
     */
    public static ErrorResponse serverError() {
        return new Builder()
                .setError("server_error")
                .build();
    }

    /**
     * The authorization server is currently unable to handle the request due to
     * a temporary overloading or maintenance of the server. (This error code is
     * needed because a 503 Service Unavailable HTTP status code cannot be
     * returned to the client via an HTTP redirect.)
     *
     * @return response
     */
    public static ErrorResponse temporarilyUnavailable() {
        return new Builder()
                .setError("temporarily_unavailable")
                .build();
    }

    /**
     * Client authentication failed (e.g., unknown client, no client
     * authentication included, or unsupported authentication method). The
     * authorization server MAY return an HTTP 401 (Unauthorized) status code to
     * indicate which HTTP authentication schemes are supported. If the client
     * attempted to authenticate via the "Authorization" request header field,
     * the authorization server MUST respond with an HTTP 401 (Unauthorized)
     * status code and include the "WWW-Authenticate" response header field
     * matching the authentication scheme used by the client.
     *
     * @return response
     */
    public static ErrorResponse invalidClient() {
        return new Builder()
                .setError("invalid_client")
                .build();
    }

    /**
     * The provided authorization grant (e.g., authorization code, resource
     * owner credentials) or refresh token is invalid, expired, revoked, does
     * not match the redirection URI used in the authorization request, or was
     * issued to another client.
     *
     * @return response
     */
    public static ErrorResponse invalidGrant() {
        return new Builder()
                .setError("invalid_grant")
                .build();
    }

    /**
     * The authenticated client is not authorized to use this authorization
     * grant type.
     *
     * @return response
     */
    public static ErrorResponse unauthorizedClient() {
        return new Builder()
                .setError("unauthorized_client")
                .build();
    }

    /**
     * The authorization grant type is not supported by the authorization
     * server.
     *
     * @return response
     */
    public static ErrorResponse unsupportedGrantType() {
        return new Builder()
                .setError("unsupported_grant_type")
                .build();
    }

    /**
     * The requested scope is invalid, unknown, malformed, or exceeds the scope
     * granted by the resource owner.
     *
     * @return response
     */
    public static ErrorResponse invalidScope() {
        return new Builder()
                .setError("invalid_scope")
                .build();
    }
}
