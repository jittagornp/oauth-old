/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.security;

import lombok.Getter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/15
 */
public interface AuthenticityToken {

    RandomOutput random();
    
    String decode(String authenticityToken);

    boolean matches(String token, String authenticityToken);

    @Getter
    public static class RandomOutput {

        private final String token;

        private final String authenticityToken;

        public RandomOutput(String token, String authenticityToken) {
            this.token = token;
            this.authenticityToken = authenticityToken;
        }

        @Override
        public String toString() {
            return "RandomOutput{" + "token=" + token + ", authenticityToken=" + authenticityToken + '}';
        }

    }

}
