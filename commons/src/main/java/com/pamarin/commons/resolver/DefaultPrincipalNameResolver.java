/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class DefaultPrincipalNameResolver implements PrincipalNameResolver {

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Override
    public String resolve(Session session) {
        Object authentication = session.getAttribute(SPRING_SECURITY_CONTEXT);
        if (authentication != null) {
            Expression expression = this.parser.parseExpression("authentication?.name");
            return expression.getValue(authentication, String.class);
        }
        return null;
    }
}
