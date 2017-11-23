/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import com.pamarin.commons.exception.UnsupportedPrivateConstructorException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static java.lang.reflect.Modifier.isPrivate;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/23
 */
public class ClassUtils {

    private ClassUtils() {
        throw new UnsupportedPrivateConstructorException();
    }

    public static boolean isPrivateConstructor(Class<?> clazz) {
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        if (isPrivate(constructor.getModifiers())) {
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                return ex.getCause() instanceof UnsupportedPrivateConstructorException;
            }
        }
        return false;
    }

}
