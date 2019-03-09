/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

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
public class HttpClientIPAddressResolverTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private HttpClientIPAddressResolver resolver;

    private HttpServletRequest httpReq;

    @Before
    public void before() {
        resolver = new DefaultHttpClientIPAddressResolver();
        httpReq = mock(HttpServletRequest.class);
    }

    @Test
    public void shouldBeThrowIllegalArgumentException_whenInputIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("require httpReq.");

        resolver.resolve(null);
    }

    @Test
    public void shouldBeNull_whenAnyHeadersIsNull() {
        when(httpReq.getRemoteAddr()).thenReturn(null);
        String output = resolver.resolve(httpReq);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenXForwardedForHeaderIsUnknown() {
        when(httpReq.getHeader("X-Forwarded-For")).thenReturn("UNKNOWN");
        String output = resolver.resolve(httpReq);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe127_0_0_1_whenXForwardedForHeaderIs127_0_0_1() {
        when(httpReq.getHeader("X-Forwarded-For")).thenReturn("127.0.0.1");
        String output = resolver.resolve(httpReq);
        String expected = "127.0.0.1";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe127_0_0_1_whenRemoteAddrIs127_0_0_1() {
        when(httpReq.getRemoteAddr()).thenReturn("127.0.0.1");
        String output = resolver.resolve(httpReq);
        String expected = "127.0.0.1";
        assertThat(output).isEqualTo(expected);
    }

}
