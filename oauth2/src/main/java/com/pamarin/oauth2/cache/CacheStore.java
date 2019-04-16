/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.cache;

import java.io.Serializable;

/**
 *
 * @author jitta
 * @param <T>
 */
public interface CacheStore<T extends Serializable> {

    int getExpiresMinutes();

    void cache(String key, T value);

    T get(String key);

    void delete(String key);
}
