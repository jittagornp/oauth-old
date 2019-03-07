/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.interceptor;

import com.pamarin.commons.util.CookieSpecBuilder;
import com.pamarin.oauth2.domain.UserSource;
import com.pamarin.oauth2.resolver.UserSourceTokenIdResolver;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.pamarin.oauth2.repository.UserSourceRepo;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/07
 */
public class UserSourceTokenInterceptor extends HandlerInterceptorAdapter {

    private final String cookieName;

    @Autowired
    private UserSourceRepo userSourceRepo;

    @NotBlank
    @Value("${server.hostUrl}")
    private String hostUrl;

    private final UserSourceTokenIdResolver userSourceTokenIdResolver;

    public UserSourceTokenInterceptor(String cookieName, UserSourceTokenIdResolver userSourceTokenIdResolver) {
        this.cookieName = cookieName;
        this.userSourceTokenIdResolver = userSourceTokenIdResolver;
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
        String id = UUID.randomUUID().toString();
        UserSource userSource = new UserSource();
        userSource.setId(id);
        userSourceRepo.save(userSource);

        return Base64.getEncoder().encodeToString(id.getBytes());
    }

    private boolean hasSourceCookie(HttpServletRequest httpReq) {
        return hasText(userSourceTokenIdResolver.resolve(httpReq));
    }
}
