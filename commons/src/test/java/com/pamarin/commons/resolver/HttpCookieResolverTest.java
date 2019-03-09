/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author jitta
 */
public class HttpCookieResolverTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private HttpCookieResolver resolver;

    private HttpServletRequest httpReq;

    @Before
    public void before() {
        httpReq = mock(HttpServletRequest.class);
        resolver = new DefaultHttpCookieResolver("user-session");
    }

    @Test
    public void shouldBeThrowIllegalArgumentException_whenInputIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("require httpReq.");

        resolver.resolve(null);
    }

    @Test
    public void shouldBeNull_whenEmptyCookie() {
        String output = resolver.resolve(httpReq);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenCookieIsNull() {
        when(httpReq.getCookies()).thenReturn(new Cookie[]{null});
        String output = resolver.resolve(httpReq);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeNull_whenCookieNameNotFound() {
        String cookieValue = UUID.randomUUID().toString();
        when(httpReq.getCookies()).thenReturn(new Cookie[]{new Cookie("user-source", cookieValue)});
        String output = resolver.resolve(httpReq);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk() {
        String cookieValue = UUID.randomUUID().toString();
        when(httpReq.getCookies()).thenReturn(new Cookie[]{new Cookie("user-session", cookieValue)});
        String output = resolver.resolve(httpReq);
        String expected = cookieValue;
        assertThat(output).isEqualTo(expected);
    }
}
