/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import static com.pamarin.commons.util.DateConverterUtils.convert2Date;
import static java.security.MessageDigest.isEqual;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class DefaultHashBasedToken implements HashBasedToken {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHashBasedToken.class);

    private final String key;

    private final CheckSum checkSum;

    public DefaultHashBasedToken(String key, CheckSum checkSum) {
        this.key = key;
        this.checkSum = checkSum;
    }

    private String hash(UserDetails userDetails, long expiresTimpstamp) {
        return checkSum.hash((userDetails.getUsername() + ":"
                + expiresTimpstamp + ":"
                + userDetails.getPassword() + ":"
                + this.key).getBytes());
    }

    /*
    token = base64(username + ":" + expirationTime + ":" + checksum.hash(username + ":" + expirationTime + ":" password + ":" + key))

    username:          As identifiable to the UserDetailsService
    password:          That matches the one in the retrieved UserDetails
    expirationTime:    The date and time when the token expires,
                       expressed in milliseconds
    key:               A private key to prevent modification of the token
      
     */
    @Override
    public String hash(UserDetails userDetails, LocalDateTime expires) {
        long timpstamp = convert2Date(expires).getTime();
        return base64Encode(
                userDetails.getUsername() + ":"
                + timpstamp + ":"
                + hash(userDetails, timpstamp)
        );
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

        String[] arr = StringUtils.split(decoded, ":");
        if (arr.length != 3) {
            return false;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(arr[0]);
        if (userDetails == null) {
            return false;
        }

        try {
            long timpstamp = Long.parseLong(arr[1]);
            if (wasExpires(timpstamp)) {
                return false;
            }
            return isEqual(arr[2].getBytes(), hash(userDetails, timpstamp).getBytes());
        } catch (NumberFormatException ex) {
            LOG.warn("Can't parse expirationTime.", ex);
            return false;
        }
    }

    private boolean wasExpires(long timpstamp) {
        return timpstamp > convert2Date(LocalDateTime.now()).getTime();
    }

}
