/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

/**
 *
 * @author jitta
 */
public interface CacheStore<T> {

    int getExpiresMinutes();

    void cache(String key, T value);

    T get(String key);

    void delete(String key);
}
