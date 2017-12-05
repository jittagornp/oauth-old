/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.exception;

import com.pamarin.commons.exception.AuthenticationException;
import com.pamarin.commons.exception.InvalidCsrfTokenException;
import com.pamarin.oauth2.model.ErrorResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/03
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public void invalidRequest(BindException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse.invalidRequest()
                .returnError(request, response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidResponseTypeException.class)
    public void unsupportedResponseType(InvalidResponseTypeException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse.unsupportedResponseType()
                .returnError(request, response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServletRequestBindingException.class)
    public void invalidRequest(ServletRequestBindingException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse err = ErrorResponse.invalidRequest();
        if (ex.getMessage().startsWith("Missing request header 'Authorization'")) {
            err.setErrorDescription("Require header 'Authorization' as http basic");
        } else {
            err.setErrorDescription(ex.getMessage());
        }
        err.returnError(request, response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    public void invalidRequest(UnsatisfiedServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String params = ex.getParamConditionGroups().stream().map(m -> StringUtils.join(m, ",")).sorted().collect(Collectors.joining(" or "));
        ErrorResponse err = params.contains("grant_type") ? ErrorResponse.invalidGrant() : ErrorResponse.invalidRequest();
        err.setErrorDescription("Require parameter '" + params + "'");
        err.returnError(request, response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRedirectUriException.class)
    public void invalidRequest(InvalidRedirectUriException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse err = ErrorResponse.invalidRequest();
        err.setErrorDescription("Invalid format '" + ex.getRedirectUri() + "'");
        err.returnError(request, response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public void invalidRequest(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse err = ErrorResponse.invalidRequest();
        err.setErrorDescription("Require parameter " + ex.getParameterName() + " (" + ex.getParameterType() + ")");
        err.returnError(request, response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public void invalidRequest(HttpMediaTypeNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse err = ErrorResponse.invalidRequest();
        err.setErrorDescription("Not support media type '" + ex.getContentType() + "'");
        err.returnError(request, response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void invalidRequest(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse err = ErrorResponse.invalidRequest();
        err.setErrorDescription("Not support http '" + ex.getMethod() + "'");
        err.returnError(request, response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidClientIdException.class)
    public void invalidClient(InvalidClientIdException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse.invalidClient()
                .returnError(request, response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidScopeException.class)
    public void invalidScope(InvalidScopeException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse.invalidScope()
                .returnError(request, response);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedClientException.class)
    public void unauthorizedClient(UnauthorizedClientException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse.unauthorizedClient()
                .returnError(request, response);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidClientIdAndClientSecretException.class)
    public void invalidCsrfToken(InvalidClientIdAndClientSecretException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse.unauthorizedClient()
                .returnError(request, response);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public void unauthorizedClient(AuthenticationException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorResponse.unauthorizedClient()
                .returnError(request, response);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidCsrfTokenException.class)
    public void invalidCsrfToken(InvalidCsrfTokenException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        new ErrorResponse.Builder()
                .setError("invalid_csrf_token")
                .build()
                .returnError(request, response);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public void serverError(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.warn(null, ex);
        ErrorResponse.serverError()
                .returnError(request, response);
    }

}
