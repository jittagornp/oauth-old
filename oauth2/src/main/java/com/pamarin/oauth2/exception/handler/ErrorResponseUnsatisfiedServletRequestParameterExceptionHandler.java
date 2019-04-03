/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.exception.handler;

import com.pamarin.oauth2.model.ErrorResponse;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseUnsatisfiedServletRequestParameterExceptionHandler extends ErrorResponseExceptionHandlerAdapter<UnsatisfiedServletRequestParameterException> {

    @Override
    public Class<UnsatisfiedServletRequestParameterException> getTypeClass() {
        return UnsatisfiedServletRequestParameterException.class;
    }

    @Override
    protected ErrorResponse buildError(UnsatisfiedServletRequestParameterException ex, HttpServletRequest httpReq, HttpServletResponse httpResp) {
        String params = ex.getParamConditionGroups().stream().map(m -> StringUtils.join(m, ",")).sorted().collect(Collectors.joining(" or "));
        ErrorResponse err = params.contains("grant_type") ? ErrorResponse.invalidGrant() : ErrorResponse.invalidRequest();
        err.setErrorDescription("Require parameter '" + params + "'");
        return err;
    }

}
