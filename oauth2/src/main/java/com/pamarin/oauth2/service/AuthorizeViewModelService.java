/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.service;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/22
 */
public interface AuthorizeViewModelService {

    Model findByClientIdAndScopes(String clientId, List<String> scopes);

    @Getter
    @Setter
    @Builder
    public static class Model {

        private String userName;

        private String clientName;

        private List<Scope> scopes;

        public List<Scope> getScopes() {
            if (scopes == null) {
                scopes = new ArrayList<>();
            }
            return scopes;
        }

    }

    @Getter
    @Setter
    @Builder
    public static class Scope {

        private String id;

        private String description;

    }

}
