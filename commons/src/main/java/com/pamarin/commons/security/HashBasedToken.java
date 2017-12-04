/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public interface HashBasedToken {

    String hash(Credential credential, LocalDateTime expires);

    boolean matches(String token, Credential credential);

    @Getter
    @Setter
    @Builder
    public static class Credential {

        private String username;

        private String password;

    }
}
