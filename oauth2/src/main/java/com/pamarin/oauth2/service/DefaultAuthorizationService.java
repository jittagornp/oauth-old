/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.exception.RequireApprovalException;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.AuthorizationResponse;
import com.pamarin.commons.provider.HostUrlProvider;
import com.pamarin.commons.util.QuerystringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pamarin.commons.security.hashing.StringSignature;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/25
 */
@Service
@Transactional
class DefaultAuthorizationService implements AuthorizationService {

    @Autowired
    private HostUrlProvider hostUrlProvider;

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private AuthorizationCodeGenerator authorizationCodeGenerator;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private AuthorizationRequestVerification requestVerification;

    @Autowired
    private StringSignature stringSignature;

    private boolean wasApprovedClient(String clientId) {
        return approvalService.wasApprovedByUserIdAndClientId(
                loginSession.getUserDetails().getUsername(),
                clientId
        );
    }

    @Override
    public String authorize(AuthorizationRequest req) {
        requestVerification.verify(req);
        if (loginSession.wasCreated()) {
            if (!wasApprovedClient(req.getClientId())) {
                throw new RequireApprovalException();
            }
            return obtainingAuthorization(req);
        } else {
            String querystring = req.buildQuerystring();
            String signature = stringSignature.sign(querystring);
            return hostUrlProvider.provide() + "/login?" + querystring + "&signature=" + signature;
        }
    }

    private String obtainingAuthorization(AuthorizationRequest req) {
        return generateAuthorizationCode(req);
    }

    //https://tools.ietf.org/html/rfc6749#section-4.1.2
    private String generateAuthorizationCode(AuthorizationRequest req) {
        AuthorizationResponse resp = authorizationCodeGenerator.generate(req);
        resp.setState(req.getState());
        String uri = req.getRedirectUri();
        return uri + (uri.contains("?") ? "&" : "?") + resp.buildQuerystring();
    }

    @Override
    public String approved(AuthorizationRequest req) {
        requestVerification.verify(req);
        approvalService.approvedClientByUserId(req, loginSession.getUserDetails().getUsername());
        return hostUrlProvider.provide() + "/authorize?" + req.buildQuerystring();
    }

    @Override
    public String notApprove(AuthorizationRequest req) {
        requestVerification.verify(req);
        String uri = req.getRedirectUri();
        return uri + (uri.contains("?") ? "&" : "?")
                + new QuerystringBuilder()
                        .addParameter("error", "not_approve")
                        .addParameter("error_status", "403")
                        .addParameter("state", req.getState())
                        .build();
    }

}
