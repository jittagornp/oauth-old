/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.service;

import com.pamarin.oauth2.LoginServiceImpl;
import com.pamarin.oauth2.domain.User;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.pamarin.oauth2.repository.UserRepository;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class LoginService_loadUserByUsernameTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        when(userRepository.findOne("1111"))
                .thenReturn(null);
        when(userRepository.findOne("2222"))
                .thenReturn(new User());
    }

    @Test
    public void shouldBeErrorRequireId_whenInputIsNull() {

        exception.expect(UsernameNotFoundException.class);
        exception.expectMessage("Require id.");

        String input = null;
        UserDetails output = loginService.loadUserByUsername(input);

    }
    
    @Test
    public void shouldBeErrorUserNotFound_whenInputIsTest() {

        exception.expect(UsernameNotFoundException.class);
        exception.expectMessage("User not found.");

        String input = "1111";
        UserDetails output = loginService.loadUserByUsername(input);

    }
    
    @Test
    public void shouldBeOk() {
        String input = "2222";
        UserDetails output = loginService.loadUserByUsername(input);
        assertThat(output).isNotNull();
    }

}
