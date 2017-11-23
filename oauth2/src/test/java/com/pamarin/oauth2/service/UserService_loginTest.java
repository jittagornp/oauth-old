/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.UserServiceImpl;
import com.pamarin.oauth2.domain.User;
import com.pamarin.oauth2.exception.InvalidUsernamePasswordException;
import com.pamarin.oauth2.repository.UserRepo;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.commons.security.PasswordEncryption;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/18
 */
public class UserService_loginTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncryption passwordEncryption;

    @Mock
    private LoginSession loginSession;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        when(userRepo.findByUsername("test"))
                .thenReturn(null);
        when(userRepo.findByUsername("root"))
                .thenReturn(new User());
    }

    @Test
    public void shouldBeErrorRequireUsernameAndPassword_whenUsernameAndPasswordIsNull() {

        exception.expect(InvalidUsernamePasswordException.class);
        exception.expectMessage("Require username and password.");

        String username = null;
        String password = null;
        userService.login(username, password);
    }

    @Test
    public void shouldBeErrorRequireUsernameAndPassword_whenPasswordIsNull() {

        exception.expect(InvalidUsernamePasswordException.class);
        exception.expectMessage("Require username and password.");

        String username = "test";
        String password = null;
        userService.login(username, password);
    }

    @Test
    public void shouldBeErrorUserNotFound_whenUsernameAndPasswordIsTest() {

        exception.expect(InvalidUsernamePasswordException.class);
        exception.expectMessage("User not found.");

        String username = "test";
        String password = "test";
        userService.login(username, password);
    }

    @Test
    public void shouldBeErrorPasswordNotMatch_whenUsernameAndPasswordIsRoot() {

        exception.expect(InvalidUsernamePasswordException.class);
        exception.expectMessage("Password not match.");

        when(passwordEncryption.matches(any(String.class), any(String.class)))
                .thenReturn(false);

        String username = "root";
        String password = "root";
        userService.login(username, password);
    }

    @Test
    public void shouldBeOk() {
        when(passwordEncryption.matches(any(String.class), any(String.class)))
                .thenReturn(true);

        String username = "root";
        String password = "password";
        userService.login(username, password);
    }
}
