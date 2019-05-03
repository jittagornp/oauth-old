/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.domain.User;
import com.pamarin.oauth2.exception.InvalidUsernamePasswordException;
import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.commons.security.PasswordEncryption;
import com.pamarin.oauth2.service.LoginHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pamarin.oauth2.service.LoginService;
import com.pamarin.oauth2.service.RevokeSessionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.pamarin.oauth2.repository.UserRepository;
import com.pamarin.oauth2.ratelimit.LoginRateLimitService;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; createSession : 2017/11/12
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncryption passwordEncryption;

    @Autowired
    private LoginSession loginSession;

    @Autowired
    private RevokeSessionService revokeSessionService;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private LoginRateLimitService loginRateLimitService;

    @Override
    public void login(String username, String password) {
        if (username == null || password == null) {
            throw new InvalidUsernamePasswordException("Require username and password.");
        }

        loginRateLimitService.limit(username);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidUsernamePasswordException("User not found.");
        }

        if (!passwordEncryption.matches(password, user.getPassword())) {
            throw new InvalidUsernamePasswordException("Password not match.");
        }

        loginSession.create(convert(user));
        revokeSessionService.revokeOthersOnSameUserAgentBySessionId(loginSession.getSessionId());
        loginHistoryService.createHistory();
    }

    @Override
    public UserDetails loadUserByUsername(String id) {
        if (id == null) {
            throw new UsernameNotFoundException("Require id.");
        }

        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }

        return convert(user);
    }

    private UserDetails convert(User user) {
        return DefaultUserDetails.builder()
                .username(user.getId())
                .password(user.getPassword())
                .authorities(null)
                .build();
    }

}
