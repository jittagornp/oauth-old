/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pamarin.oauth2.AppStarter;
import com.pamarin.oauth2.IntegrationTestBase;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppStarter.class)
public class JwtSignerTest extends IntegrationTestBase {

    @Autowired
    private RSAPublicKeyReader publicKeyReader;

    @Autowired
    private RSAPrivateKeyReader privateKeyReader;

    @Test
    public void shouldBeOk() {
        //RSA
        RSAPublicKey publicKey = publicKeyReader.readFromDERFile(getClass().getResourceAsStream("/key/public-key.der"));
        RSAPrivateKey privateKey = privateKeyReader.readFromDERFile(getClass().getResourceAsStream("/key/private-key.der"));
        Algorithm algorithmRS = Algorithm.RSA256(publicKey, privateKey);

        String token = JWT.create()
                .withIssuer("jittagornp")
                .sign(algorithmRS);

        JWTVerifier verifier = JWT.require(algorithmRS)
                .withIssuer("jittagornp")
                .build(); 
        DecodedJWT jwt = verifier.verify(token);

        assertThat(jwt.getIssuer()).isEqualTo("jittagornp");
    }

}
