/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.config;

import com.pamarin.oauth2.AppStarter;
import com.pamarin.oauth2.IntegrationTestBase;
import com.pamarin.commons.security.PasswordEncryption;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppStarter.class)
public class PasswordEncoderTest extends IntegrationTestBase {

    @Autowired
    private PasswordEncryption passwordEncryption;

    @Test
    public void shouldBeOk() {
        String input = "password";
        String output = passwordEncryption.encrypt(input);
        boolean expected = true;
        System.out.println("input => " + input + ", output => " + output);
        assertThat(passwordEncryption.matches(input, output)).isEqualTo(expected);
    }

}
