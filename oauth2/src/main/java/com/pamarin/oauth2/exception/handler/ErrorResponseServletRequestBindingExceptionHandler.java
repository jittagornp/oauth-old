/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseServletRequestBindingExceptionHandler extends ErrorResponseExceptionHandlerAdapter<ServletRequestBindingException>{

    @Override
    public Class<ServletRequestBindingException> getTypeClass() {
        return ServletRequestBindingException.class;
    }

    @Override
    protected ErrorResponse buildError(ServletRequestBindingException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        ErrorResponse err = ErrorResponse.invalidRequest();
        if (ex.getMessage().startsWith("Missing request header 'Authorization'")) {
            err.setErrorDescription("Require header 'Authorization' as http basic");
        } else {
            err.setErrorDescription(ex.getMessage());
        }
        return err;
    }
    
}
