/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/23
 */
public class AuthorizationRequest_getScopesTest {

    @Test
    public void shouldBeEmptyList_whenScopeIsNull() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .scope(null)
                .build();
        List<String> output = input.getScopes();
        List<String> expected = Collections.emptyList();
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeEmptyList_whenScopeIsEmptyString() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .scope("")
                .build();
        List<String> output = input.getScopes();
        List<String> expected = Collections.emptyList();
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe1Element_whenScopeIsRead() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .scope("read")
                .build();
        List<String> output = input.getScopes();
        List<String> expected = Arrays.asList("read");
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe2Elements_whenScopeIsReadAndWrite() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .scope("read,write")
                .build();
        List<String> output = input.getScopes();
        List<String> expected = Arrays.asList("read", "write");
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe3Elements_whenScopeIsReadWriteAndDelete() {
        AuthorizationRequest input = AuthorizationRequest.builder()
                .scope("read,write,delete")
                .build();
        List<String> output = input.getScopes();
        List<String> expected = Arrays.asList("read", "write", "delete");
        assertThat(output).isEqualTo(expected);
    }

}
