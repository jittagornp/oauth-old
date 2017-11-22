/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import com.pamarin.commons.exception.InvalidHttpBasicAuthenException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/09/26
 */
@Component
class DefaultHttpBasicAuthenParser implements HttpBasicAuthenParser {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHttpBasicAuthenParser.class);

    @Override
    public Output parse(String authorization) {
        if (!hasText(authorization)) {
            throw new InvalidHttpBasicAuthenException("Required authorization.");
        }

        Pattern pattern = Pattern.compile("^basic\\s(.*?)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(authorization);
        while (matcher.find()) {
            return convert(matcher.group(1));
        }

        throw new InvalidHttpBasicAuthenException("Invalid Credential value (Required Basic Authen).");
    }

    private Output convert(String credential) {
        if (!hasText(credential)) {
            throw new InvalidHttpBasicAuthenException("Invalid Credential value (Empty value).");
        }

        byte[] bytes;
        try {
            bytes = Base64Utils.decodeFromString(credential);
        } catch (IllegalArgumentException ex) {
            LOG.debug(null, ex);
            throw new InvalidHttpBasicAuthenException("Invalid Credential value (Can't decode base64).");
        }

        String decoded = new String(bytes, Charset.forName("utf-8"));
        String[] split = StringUtils.split(decoded, ":");
        if (split == null || split.length != 2) {
            throw new InvalidHttpBasicAuthenException("Invalid Credential value (Invalid basic authen format).");
        }

        return new Output(split[0], split[1]);
    }

}
