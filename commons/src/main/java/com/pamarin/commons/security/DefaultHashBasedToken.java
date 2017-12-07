/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.security.hashing.Hashing;
import static com.pamarin.commons.util.DateConverterUtils.convert2Date;
import java.time.LocalDateTime;
import java.util.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class DefaultHashBasedToken implements HashBasedToken {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHashBasedToken.class);

    private static final String SEPARATOR = ":";

    private final String privateKey;

    private final Hashing hashing;

    /**
     * for hmac algorithm such as hmacsha384
     * 
     * @param hashing 
     */
    public DefaultHashBasedToken(Hashing hashing) {
        this(null, hashing);
    }

    /**
     * for normal algorithm such as sha384
     * 
     * @param privateKey
     * @param hashing 
     */
    public DefaultHashBasedToken(String privateKey, Hashing hashing) {
        this.privateKey = privateKey;
        this.hashing = hashing;
    }

    private String rawSignature(UserDetails userDetails, long expiresTimpstamp) {
        StringBuilder builder = new StringBuilder()
                .append(userDetails.getUsername())
                .append(SEPARATOR)
                .append(expiresTimpstamp)
                .append(SEPARATOR)
                .append(userDetails.getPassword())
                .append(SEPARATOR);
        if (hasText(this.privateKey)) {
            builder.append(this.privateKey);
        }
        return builder.toString();
    }

    /*
    token = base64(username + ":" + expirationTime + ":" + checksum.hash(username + ":" + expirationTime + ":" password + ":" + privateKey))

    username:          As identifiable to the UserDetailsService
    password:          That matches the one in the retrieved UserDetails
    expirationTime:    The date and time when the token expires, expressed in milliseconds
    privateKey:        A private key to prevent modification of the token
      
     */
    @Override
    public String hash(UserDetails userDetails, LocalDateTime expires) {
        long timpstamp = convert2Date(expires).getTime();
        return base64Encode(new StringBuilder()
                .append(userDetails.getUsername())
                .append(SEPARATOR)
                .append(timpstamp)
                .append(SEPARATOR)
                .append(hashing.hash(rawSignature(userDetails, timpstamp).getBytes()))
                .toString());
    }

    private String base64Encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    private String base64Decode(String token) {
        try {
            return new String(Base64.getDecoder().decode(token));
        } catch (IllegalArgumentException ex) {
            LOG.warn("Can't decode base64 token.", ex);
            return null;
        }
    }

    @Override
    public boolean matches(String token, UserDetailsService userDetailsService) {
        String decoded = base64Decode(token);
        if (decoded == null) {
            return false;
        }

        String[] arr = StringUtils.split(decoded, SEPARATOR);
        if (arr.length != 3) {
            return false;
        }

        String username = arr[0];
        String expires = arr[1];
        String signature = arr[2];

        long timpstamp;
        try {
            timpstamp = Long.parseLong(expires);
        } catch (NumberFormatException ex) {
            LOG.warn("Can't parse expirationTime.", ex);
            return false;
        }

        if (wasExpires(timpstamp)) {
            return false;
        }

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            LOG.warn("user not found", ex);
            return false;
        }

        return hashing.matches(rawSignature(userDetails, timpstamp).getBytes(), signature);
    }

    private boolean wasExpires(long timpstamp) {
        return timpstamp < convert2Date(LocalDateTime.now()).getTime();
    }

}
