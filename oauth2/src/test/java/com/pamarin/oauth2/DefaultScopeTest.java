/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.service.DefaultScope;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/23
 */
public class DefaultScopeTest {

    private DefaultScope defaultScope;

    @Before
    public void before() {
        defaultScope = new DefaultScopeImpl();
    }

    @Test
    public void test() {
        String output = defaultScope.getDefault();
        String expected = "user:public_profile";
        assertThat(output).isEqualTo(expected);
    }

}
