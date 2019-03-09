/*
 * Copyright 2017-2019 Pamarin.com
 */

package com.pamarin.oauth2.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp <http://jittagornp.me>  
 * create : 2017/10/10
 */
public class AuthorizationResponse_buildQuerystringTest {

    @Test
    public void shouldBeOnlyCodeParameter(){
        AuthorizationResponse input = AuthorizationResponse.builder()
                .code("AAA")
                .build();
        String output = input.buildQuerystring();
        String expected = "code=AAA";
        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldBeHasCodeAndStateParameters(){
        AuthorizationResponse input = AuthorizationResponse.builder()
                .code("AAA")
                .state("XYZ")
                .build();
        String output = input.buildQuerystring();
        String expected = "code=AAA&state=XYZ";
        assertThat(output).isEqualTo(expected);
    }
    
}
