/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/16
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
public @interface GetCsrfToken {

}
