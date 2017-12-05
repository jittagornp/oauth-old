/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import static com.pamarin.commons.util.DateConverterUtils.convert2Date;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class DefaultHashBasedToken implements HashBasedToken {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHashBasedToken.class);

    private static final String SEPARATOR = ":";

    private int padLength;

    private final String privateKey;

    private final CheckSum checkSum;

    private final SecureRandom secureRandom;

    public DefaultHashBasedToken(String key, CheckSum checkSum) {
        this(key, checkSum, 32);
    }

    public DefaultHashBasedToken(String privateKey, CheckSum checkSum, int padLength) {
        this.privateKey = privateKey;
        this.checkSum = checkSum;
        this.padLength = padLength;
        this.secureRandom = new SecureRandom();
    }

    private String rawData(String oneTimePad, UserDetails userDetails, long expiresTimpstamp) {
        return new StringBuilder()
                .append(oneTimePad)
                .append(SEPARATOR)
                .append(userDetails.getUsername())
                .append(SEPARATOR)
                .append(expiresTimpstamp)
                .append(SEPARATOR)
                .append(userDetails.getPassword())
                .append(SEPARATOR)
                .append(this.privateKey)
                .toString();
    }

    //https://www.wikiwand.com/en/One-time_pad
    private String randomOneTimePad() {
        byte[] bytes = new byte[padLength];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /*
    token = base64(oneTimePad + ":" + username + ":" + expirationTime + ":" + checksum.hash(oneTimePad + ":" + username + ":" + expirationTime + ":" password + ":" + privateKey))

    oneTimePad:        Random one time token 
    username:          As identifiable to the UserDetailsService
    password:          That matches the one in the retrieved UserDetails
    expirationTime:    The date and time when the token expires, expressed in milliseconds
    privateKey:        A private key to prevent modification of the token
      
     */
    @Override
    public String hash(UserDetails userDetails, LocalDateTime expires) {
        long timpstamp = convert2Date(expires).getTime();
        String oneTimePad = randomOneTimePad();
        return base64Encode(new StringBuilder()
                .append(oneTimePad)
                .append(SEPARATOR)
                .append(userDetails.getUsername())
                .append(SEPARATOR)
                .append(timpstamp)
                .append(SEPARATOR)
                .append(checkSum.hash(rawData(oneTimePad, userDetails, timpstamp).getBytes()))
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
        if (arr.length != 4) {
            return false;
        }

        String oneTimePad = arr[0];
        String username = arr[1];
        String expires = arr[2];
        String hashValue = arr[3];

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

        return checkSum.matches(rawData(oneTimePad, userDetails, timpstamp).getBytes(), hashValue);
    }

    private boolean wasExpires(long timpstamp) {
        return timpstamp < convert2Date(LocalDateTime.now()).getTime();
    }

}
