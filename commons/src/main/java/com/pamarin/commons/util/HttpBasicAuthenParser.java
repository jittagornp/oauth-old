/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/26
 */
@FunctionalInterface
public interface HttpBasicAuthenParser {

    Output parse(String authorization);

    public static class Output {

        private final String username;

        private final String password;

        public Output(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

}
