/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp <http://jittagornp.me>
 * create : 2017/10/03
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ResponseType.Validator.class)
public @interface ResponseType {

    String message() default "invalid type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Component
    public static class Validator implements ConstraintValidator<ResponseType, String> {

        @Override
        public void initialize(ResponseType a) {
            //Not used
        }

        @Override
        public boolean isValid(String type, ConstraintValidatorContext context) {
            if (!hasText(type)) {
                return false;
            }

            return "code".equals(type);
        }

        public boolean isValid(String type) {
            return isValid(type, null);
        }

    }

}
