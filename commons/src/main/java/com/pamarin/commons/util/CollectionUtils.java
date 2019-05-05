/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import static java.util.Collections.frequency;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author jitta
 */
public class CollectionUtils {

    private CollectionUtils() {

    }

    public static <T> Map<T, Integer> countDuplicateItems(List<T> list) {
        Map<T, Integer> result = new LinkedHashMap<>();
        Set<T> set = new HashSet<>(list);
        set.forEach(item -> result.put(item, frequency(list, item)));
        return result;
    }
}
