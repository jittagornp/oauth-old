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
public class DefaultUUIDGenerator implements UUIDGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }

}
