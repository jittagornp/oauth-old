/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.generator;

import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class DefaultErrorCodeGenerator implements ErrorCodeGenerator {

    private static final int CODE_SIZE = 8;

    @Override
    public String generate() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase()
                .substring(0, CODE_SIZE);
    }

}
