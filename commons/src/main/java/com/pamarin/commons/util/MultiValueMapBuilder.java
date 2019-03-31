/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author jitta
 * @param <K> key type
 * @param <V> value type
 */
public class MultiValueMapBuilder<K, V> {

    private final MultiValueMap<K, V> map;

    private MultiValueMapBuilder(MultiValueMap<K, V> map) {
        this.map = map;
    }

    public static <K, V> MultiValueMapBuilder<K, V> newLinkedMultiValueMap() {
        return new MultiValueMapBuilder<>(new LinkedMultiValueMap<>());
    }

    public MultiValueMapBuilder add(K key, V value) {
        map.add(key, value);
        return this;
    }

    public MultiValueMap<K, V> build() {
        return map;
    }

}
