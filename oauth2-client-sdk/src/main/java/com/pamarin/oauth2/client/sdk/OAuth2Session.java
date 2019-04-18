/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Session {

    private String id;

    private long issuedAt;

    private long expiresAt;

    private User user;

    private Client client;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User implements Serializable {

        private String id;

        private String name;

        private List<String> authorities;

        public List<String> getAuthorities() {
            if (authorities == null) {
                authorities = new ArrayList<>();
            }
            return authorities;
        }

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Client implements Serializable {

        private String id;

        private String name;

        private List<String> scopes;

        public List<String> getScopes() {
            if (scopes == null) {
                scopes = new ArrayList<>();
            }
            return scopes;
        }
    }
}
