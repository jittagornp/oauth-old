/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class DefaultHashBasedToken implements HashBasedToken {

    private final String key;

    private final CheckSum checkSum;

    public DefaultHashBasedToken(String key, CheckSum checkSum) {
        this.key = key;
        this.checkSum = checkSum;
    }

    /*
    token = base64(username + ":" + expirationTime + ":" + checksum.hash(username + ":" + expirationTime + ":" password + ":" + key))

    username:          As identifiable to the UserDetailsService
    password:          That matches the one in the retrieved UserDetails
    expirationTime:    The date and time when the remember-me token expires,
                       expressed in milliseconds
    key:               A private key to prevent modification of the remember-me token
      
     */
    @Override
    public String hash(Credential credential, LocalDateTime expires) {
        long expiresTimpstamp = Timestamp.valueOf(expires).getTime();
        String plain = credential.getUsername() + ":" + expiresTimpstamp;
        String hash = checkSum.hash((credential.getUsername() + ":"
                + expiresTimpstamp + ":"
                + credential.getPassword() + ":"
                + this.key).getBytes());
        return Base64.getEncoder().encodeToString((plain + ":" + hash).getBytes());
    }

    @Override
    public boolean matches(String token, Credential credential) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
