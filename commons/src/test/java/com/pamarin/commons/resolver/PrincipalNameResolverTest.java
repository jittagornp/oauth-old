/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import com.pamarin.commons.security.DefaultUserDetails;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.Session;

/**
 *
 * @author jitta
 */
public class PrincipalNameResolverTest {

    private PrincipalNameResolver resolver;

    @Before
    public void before() {
        resolver = new DefaultPrincipalNameResolver();
    }

    @Test
    public void shouldBeNull_whenUserNotLogin() {
        Session input = mock(Session.class);
        String output = resolver.resolve(input);
        String expected = null;

        assertThat(output).isEqualTo(expected);
    }

    private SecurityContext getSecurityContext(String username) {
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                DefaultUserDetails.builder()
                        .username(username)
                        .build(),
                null
        ));
        return context;
    }

    @Test
    public void shouldBeJittagornp_whenUserLoggedIn() {
        Session input = mock(Session.class);
        when(input.getAttribute("SPRING_SECURITY_CONTEXT"))
                .thenReturn(getSecurityContext("jittagornp"));

        String output = resolver.resolve(input);
        String expected = "jittagornp";

        assertThat(output).isEqualTo(expected);
    }
}
