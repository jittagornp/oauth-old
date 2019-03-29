/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pamarin.oauth2.client.sdk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Builder
public class OAuth2Session {

    private String id;

    private long issuedAt;

    private long expiresAt;

    private User user;

    private Client client;

    public OAuth2Session() {
    }

    public OAuth2Session(String id, long issuedAt, long expiresAt, User user, Client client) {
        this.id = id;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.user = user;
        this.client = client;
    }

    @Getter
    @Setter
    @Builder
    public static class User implements Serializable {

        private String id;

        private String name;

        private List<String> authorities;

        public User() {
        }

        public User(String id, String name, List<String> authorities) {
            this.id = id;
            this.name = name;
            this.authorities = authorities;
        }

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
    public static class Client implements Serializable {

        private String id;

        private String name;

        private List<String> scopes;

        public Client() {
        }

        public Client(String id, String name, List<String> scopes) {
            this.id = id;
            this.name = name;
            this.scopes = scopes;
        }

        public List<String> getScopes() {
            if (scopes == null) {
                scopes = new ArrayList<>();
            }
            return scopes;
        }
    }
}
