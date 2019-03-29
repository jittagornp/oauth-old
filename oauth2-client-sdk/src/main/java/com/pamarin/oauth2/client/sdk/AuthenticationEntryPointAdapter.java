/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.oauth2.client.sdk;

import com.pamarin.commons.util.QuerystringBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 *
 * @author jitta
 */
public abstract class AuthenticationEntryPointAdapter implements AuthenticationEntryPoint {

    protected abstract String getAuthorizationServerHostUrl();

    protected abstract String getHostUrl();

    protected abstract String getClientId();

    protected abstract String getScope();

    private String getLoginUrl(HttpServletRequest httpReq) throws UnsupportedEncodingException {
        String state = UUID.randomUUID().toString();
        httpReq.getSession().setAttribute(OAuth2SdkConstant.OAUTH2_AUTHORIZATION_STATE, state);
        return "{server}/authorize?".replace("{server}", getAuthorizationServerHostUrl())
                + new QuerystringBuilder()
                        .addParameter("response_type", "code")
                        .addParameter("client_id", getClientId())
                        .addParameter("redirect_uri", getHostUrl())
                        .addParameter("scope", getScope())
                        .addParameter("state", state)
                        .build();
    }

    @Override
    public void commence(HttpServletRequest httpReq, HttpServletResponse httpResp, AuthenticationException ex) throws IOException, ServletException {
        String path = httpReq.getServletPath();
        if (isApiRequest(path)) {
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResp.setCharacterEncoding("utf-8");
            httpResp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            httpResp.getWriter().print("Unauthorized");
        } else if (isTemplateRequest(path)) {
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResp.setCharacterEncoding("utf-8");
            httpResp.setContentType(MediaType.TEXT_HTML_VALUE);
            httpResp.getWriter().print("Unauthorized");
        } else {
            httpResp.sendRedirect(getLoginUrl(httpReq));
        }
    }

    private boolean isTemplateRequest(String path) {
        return path.startsWith("/template/");
    }

    private boolean isApiRequest(String path) {
        return path.startsWith("/api/")
                || path.startsWith("/public/api/");
    }
}
