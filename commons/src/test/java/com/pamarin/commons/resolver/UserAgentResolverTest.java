/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import javax.servlet.http.HttpServletRequest;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author jitta
 */
public class UserAgentResolverTest {

    private UserAgentResolver resolver;

    private HttpServletRequest httpReq;

    @Before
    public void before() {
        httpReq = mock(HttpServletRequest.class);
        resolver = new DefaultUserAgentResolver();
    }
    
    @Test
    public void shouldBeNull_whenUserAgentIsEmptyString() {
        String userAgent = "";
        when(httpReq.getHeader("User-Agent"))
                .thenReturn(userAgent);

        UserAgent output = resolver.resolve(httpReq);
        UserAgent expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeUNKWOWN_whenUserAgentIsXXX() {
        String userAgent = "XXX";
        when(httpReq.getHeader("User-Agent"))
                .thenReturn(userAgent);

        UserAgent output = resolver.resolve(httpReq);
        UserAgent expected = UserAgent.unknown();
        assertThat(output.toString()).isEqualTo(expected.toString());
    }
    
    @Test
    public void shouldBeOk() {
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";
        when(httpReq.getHeader("User-Agent"))
                .thenReturn(userAgent);

        UserAgent output = resolver.resolve(httpReq);
        UserAgent expected = UserAgent.builder()
                .deviceTypeKey("COMPUTER")
                .deviceTypeName("Computer")
                .browserTypeKey("WEB_BROWSER")
                .browserTypeName("Browser")
                .browserKey("CHROME")
                .browserName("Chrome")
                .browserGroupKey("CHROME")
                .browserGroupName("Chrome")
                .browserRenderingEngine("WEBKIT")
                .browserManufacturerKey("GOOGLE")
                .browserManufacturerName("Google Inc.")
                .osManufacturerKey("MICROSOFT")
                .osManufacturerName("Microsoft Corporation")
                .osGroupKey("WINDOWS")
                .osGroupName("Windows")
                .osKey("WINDOWS_10")
                .osName("Windows 10")
                .agentVersion("73.0.3683.86")
                .agentMajorVersion("73")
                .agentMinorVersion("0")
                .build();
        assertThat(output.toString()).isEqualTo(expected.toString());
    }
}
