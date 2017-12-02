/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import com.pamarin.commons.exception.InvalidHttpAuthorizationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/03
 */
@Component
public class DefaultHttpAuthorizationParser implements HttpAuthorizationParser {

    @Override
    public String parse(String type, String authorization) {
        if (type == null || authorization == null) {
            throw new InvalidHttpAuthorizationException("Required type and authorization.");
        }

        Pattern pattern = Pattern.compile("^" + type + "\\s(.*?)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(authorization);
        while (matcher.find()) {
            return matcher.group(1);
        }

        throw new InvalidHttpAuthorizationException("Invalid Credential value.");
    }

}
