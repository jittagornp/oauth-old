/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.provider;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/08
 */
@FunctionalInterface
public interface HttpServletRequestProvider {

    HttpServletRequest provide();

}
