/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.util;

import static com.pamarin.commons.util.ClassUtils.isPrivateConstructor;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author jitta
 */
public class CollectionUtils_countDuplicateItemsTest {

    @Test
    public void shouldBePrivateConstructor() {
        assertTrue(isPrivateConstructor(CollectionUtils.class));
    }

    @Test
    public void shouldBeOk() {
        List<String> input = Arrays.asList("A", "B", "A", "B", "C", "A");
        Map<String, Integer> output = CollectionUtils.countDuplicateItems(input);
        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("A", 3);
        expected.put("B", 2);
        expected.put("C", 1);
        assertThat(output).isEqualTo(expected);
    }

}
