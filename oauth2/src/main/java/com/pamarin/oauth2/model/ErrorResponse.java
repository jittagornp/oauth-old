/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pamarin.commons.util.QuerystringBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/26
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("error_status")
    private Integer errorStatus;

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

    public Integer getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(Integer errorStatus) {
        this.errorStatus = errorStatus;
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

    /**
     * The request is missing a required parameter, includes an unsupported
     * parameter value (other than grant type), repeats a parameter, includes
     * multiple credentials, utilizes more than one mechanism for authenticating
     * the client, or is otherwise malformed.
     *
     * @return response
     */
    public static ErrorResponse invalidRequest() {
        return builder()
                .error("invalid_request")
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    /**
     * The resource owner or authorization server denied the request.
     *
     * @return response
     */
    public static ErrorResponse accessDenied() {
        return builder()
                .error("access_denied")
                .errorStatus(HttpStatus.FORBIDDEN.value())
                .build();
    }

    /**
     * The authorization server does not support obtaining an authorization code
     * using this method.
     *
     * @return response
     */
    public static ErrorResponse unsupportedResponseType() {
        return builder()
                .error("unsupported_response_type")
                .errorStatus(HttpStatus.BAD_REQUEST.value())
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
        return builder()
                .error("server_error")
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
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
        return builder()
                .error("temporarily_unavailable")
                .errorStatus(HttpStatus.SERVICE_UNAVAILABLE.value())
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
        return builder()
                .error("invalid_client")
                .errorStatus(HttpStatus.UNAUTHORIZED.value())
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
        return builder()
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .error("invalid_grant")
                .build();
    }

    /**
     * The authenticated client is not authorized to use this authorization
     * grant type.
     *
     * @return response
     */
    public static ErrorResponse unauthorizedClient() {
        return builder()
                .error("unauthorized_client")
                .errorStatus(HttpStatus.UNAUTHORIZED.value())
                .build();
    }

    /**
     * The authorization grant type is not supported by the authorization
     * server.
     *
     * @return response
     */
    public static ErrorResponse unsupportedGrantType() {
        return builder()
                .error("unsupported_grant_type")
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    /**
     * The requested scope is invalid, unknown, malformed, or exceeds the scope
     * granted by the resource owner.
     *
     * @return response
     */
    public static ErrorResponse invalidScope() {
        return builder()
                .error("invalid_scope")
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    public String toQueryString() {
        return new QuerystringBuilder()
                .addParameter("error", error)
                .addParameter("error_status", errorStatus == null ? null : ("" + errorStatus))
                .addParameter("error_description", errorDescription)
                .addParameter("error_uri", errorUri)
                .addParameter("state", state)
                .build();
    }
}
