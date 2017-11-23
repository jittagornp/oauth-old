/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static java.lang.reflect.Modifier.isPrivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/23
 */
public class ClassUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ClassUtils.class);

    private ClassUtils() {

    }

    public static boolean isPrivateConstructor(Class<?> clazz) {
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        if (isPrivate(constructor.getModifiers())) {
            try {
                constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOG.warn(null, ex);
                return ex.getMessage().contains("with modifiers \"private\"");
            }
        }
        return false;
    }

}
