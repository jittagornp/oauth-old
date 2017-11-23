/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.net.MalformedURLException;
import java.net.URL;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/08
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidUri.Validator.class)
public @interface ValidUri {

    String message() default "invalid format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Component
    public static class Validator implements ConstraintValidator<ValidUri, String> {

        @Override
        public void initialize(ValidUri a) {
            //Not used
        }

        @Override
        public boolean isValid(String uri, ConstraintValidatorContext context) {
            if (!hasText(uri)) {
                return true;
            }

            try {
                new URL(uri);
                return true;
            } catch (MalformedURLException ex) {
                return false;
            }
        }

        public boolean isValid(String uri) {
            return isValid(uri, null);
        }

    }

}
