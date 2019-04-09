/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import com.pamarin.commons.exception.InvalidURLException;
import java.net.MalformedURLException;
import java.net.URL;
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
public class HttpRequestOriginResolverTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private HttpRequestOriginResolver resolver;

    @Before
    public void before() {
        resolver = new DefaultHttpRequestOriginResolver();
    }

    private HttpServletRequest mockOriginHeader(String url) {
        HttpServletRequest input = mock(HttpServletRequest.class);
        when(input.getHeader("Origin")).thenReturn(url);
        return input;
    }
    
    private HttpServletRequest mockRefererHeader(String url) {
        HttpServletRequest input = mock(HttpServletRequest.class);
        when(input.getHeader("Referer")).thenReturn(url);
        return input;
    }

    @Test
    public void shouldBeThrowIllegalArgumentException_whenInputIsNull() {

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("require httpReq.");

        HttpServletRequest input = null;
        resolver.resolve(input);
    }

    @Test
    public void shouldBeInvalidURLException_whenHeaderIsNull() {

        exception.expect(InvalidURLException.class);
        exception.expectMessage("Invalid url null.");

        HttpServletRequest input = mockOriginHeader(null);
        resolver.resolve(input);
    }

    @Test
    public void shouldBeInvalidURLException_whenOriginIsXXX() {

        exception.expect(InvalidURLException.class);
        exception.expectMessage("Invalid url xxx.");

        HttpServletRequest input = mockOriginHeader("xxx");
        resolver.resolve(input);
    }

    @Test
    public void shouldBeHttpsPamarinDotCom_whenOriginIsHttpsPamarinDotCom() throws MalformedURLException {
        String url = "https://pamarin.com";
        HttpServletRequest input = mockOriginHeader(url);
        URL output = resolver.resolve(input);
        URL expected = new URL(url);
        assertThat(output).isEqualTo(expected);
        assertThat(output.getProtocol()).isEqualTo("https");
        assertThat(output.getHost()).isEqualTo("pamarin.com");
        assertThat(output.getPort()).isEqualTo(-1);
    }

    @Test
    public void shouldBeHttpsPamarinDotCom_whenRefererIsHttpsPamarinDotCom() throws MalformedURLException {
        String url = "https://pamarin.com";
        HttpServletRequest input = mockRefererHeader(url);
        URL output = resolver.resolve(input);
        URL expected = new URL(url);
        assertThat(output).isEqualTo(expected);
        assertThat(output.getProtocol()).isEqualTo("https");
        assertThat(output.getHost()).isEqualTo("pamarin.com");
        assertThat(output.getPort()).isEqualTo(-1);
    }
}
