/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.resolver;

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
public class UserSourceTokenIdResolverTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private UserSourceTokenIdResolver resolver;

    private HttpServletRequest httpReq;

    @Before
    public void before() {
        resolver = new DefaultUserSourceTokenIdResolver("user-source");
        httpReq = mock(HttpServletRequest.class);
    }

    @Test
    public void shouldBeThrowIllegalArgumentException_whenInputIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("require httpReq.");

        resolver.resolve(null);
    }

    @Test
    public void shouldBeNull_whenCookieIsNull() {
        when(httpReq.getCookies()).thenReturn(new Cookie[]{null});
        String output = resolver.resolve(httpReq);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeOk() {
        String cookieValue = "NWEwMWUxMTEtOGQyYi00ZjcyLWEwMWUtMjBiMDYyZGVhYTFi";
        when(httpReq.getCookies()).thenReturn(new Cookie[]{new Cookie("user-source", cookieValue)});
        String output = resolver.resolve(httpReq);
        String expected = "5a01e111-8d2b-4f72-a01e-20b062deaa1b";
        assertThat(output).isEqualTo(expected);
    }
}
