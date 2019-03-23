/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.generator;

import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class DefaultPrimaryKeyGenerator implements PrimaryKeyGenerator {

    @Override
    public String generate() {
        return ObjectId.get().toString();
    }
    
}
