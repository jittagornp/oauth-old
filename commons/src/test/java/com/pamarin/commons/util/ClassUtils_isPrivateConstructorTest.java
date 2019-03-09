/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import static com.pamarin.commons.util.ClassUtils.isPrivateConstructor;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/23
 */
public class ClassUtils_isPrivateConstructorTest {

    public static class EmptyConstructor {

    }

    public static class OnePublicConstructor {

        public OnePublicConstructor() {

        }
    }

    public static class PrivateConstructor {

        private PrivateConstructor() {

        }

    }

    public static class PrivateConstructorWithArguments {

        private PrivateConstructorWithArguments(String[] args) {

        }

    }

    @Test(expected = RuntimeException.class)
    public void shouldBeThrowRuntimeException() {
        isPrivateConstructor(PrivateConstructorWithArguments.class);
    }

    @Test
    public void shouldBeFalse_whenEmptyConstructor() {
        assertFalse(isPrivateConstructor(EmptyConstructor.class));
    }

    @Test
    public void shouldBeFalse_whenOnPublicConstructor() {
        assertFalse(isPrivateConstructor(OnePublicConstructor.class));
    }

    @Test
    public void shouldBeTrue_whenPrivateConstructor() {
        assertTrue(isPrivateConstructor(PrivateConstructor.class));
    }
    
    @Test
    public void shouldBePrivateConstructor() {
        assertTrue(isPrivateConstructor(ClassUtils.class));
    }
}
