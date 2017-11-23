/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import com.pamarin.commons.exception.UnsupportedPrivateConstructorException;
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

    public static class InvalidPrivateConstructor {

        private InvalidPrivateConstructor() {

        }

    }

    public static class ValidPrivateConstructor {

        private ValidPrivateConstructor() {
            throw new UnsupportedPrivateConstructorException();
        }

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
    public void shouldBeFalse_whenInvalidPrivateConstructor() {
        assertFalse(isPrivateConstructor(InvalidPrivateConstructor.class));
    }

    @Test
    public void shouldBeTrue_whenValidPrivateConstructor() {
        assertTrue(isPrivateConstructor(ValidPrivateConstructor.class));
    }

    @Test
    public void shouldBePrivateConstructor() {
        assertTrue(isPrivateConstructor(ClassUtils.class));
    }
}
