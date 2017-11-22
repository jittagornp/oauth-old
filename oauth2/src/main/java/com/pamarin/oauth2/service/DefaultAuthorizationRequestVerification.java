/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.exception.InvalidResponseTypeException;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.validator.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/12
 */
@Service
@Transactional
class DefaultAuthorizationRequestVerification implements AuthorizationRequestVerification {

    @Autowired
    private ResponseType.Validator responseTypeValidator;

    @Autowired
    private ClientVerification clientVerification;

    @Autowired
    private ScopeVerification scopeVerification;

    @Autowired
    private DefaultScope defaultScope;

    @Override
    public void verify(AuthorizationRequest req) {
        if (!responseTypeValidator.isValid(req.getResponseType())) {
            throw new InvalidResponseTypeException(req.getResponseType(), "Invalid responseType");
        }

        clientVerification.verifyClientIdAndRedirectUri(req.getClientId(), req.getRedirectUri());
        if (!hasText(req.getScope())) {
            req.setScope(defaultScope.getDefault());
        }

        scopeVerification.verifyByClientIdAndScope(req.getClientId(), req.getScope());
    }

}
