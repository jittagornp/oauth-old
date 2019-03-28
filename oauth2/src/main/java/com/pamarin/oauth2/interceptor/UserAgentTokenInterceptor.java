/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.interceptor;

import com.pamarin.commons.util.CookieSpecBuilder;
import java.time.LocalDateTime;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.pamarin.oauth2.resolver.UserAgentTokenIdResolver;
import org.springframework.beans.factory.annotation.Autowired;
import com.pamarin.commons.generator.IdGenerator;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/07
 */
public class UserAgentTokenInterceptor extends HandlerInterceptorAdapter {

    private final String cookieName;

    @NotBlank
    @Value("${server.hostUrl}")
    private String hostUrl;

    @Autowired
    private UserAgentTokenIdResolver userAgentTokenIdResolver;
    
    @Autowired
    private IdGenerator idGenerator;

    public UserAgentTokenInterceptor(String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public void postHandle(HttpServletRequest httpReq, HttpServletResponse httpResp, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !hasSourceCookie(httpReq)) {
            httpResp.setHeader("Set-Cookie", new CookieSpecBuilder(cookieName, makeToken())
                    .setPath("/")
                    .setSecure(hostUrl.startsWith("https://"))
                    .setExpires(LocalDateTime.now().plusYears(100))
                    .build());
        }
    }

    private String makeToken() {
        String id = idGenerator.generate();
        return Base64.getEncoder().encodeToString(id.getBytes());
    }

    private boolean hasSourceCookie(HttpServletRequest httpReq) {
        return hasText(userAgentTokenIdResolver.resolve(httpReq));
    }
}
