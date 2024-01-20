package com.example.onetimemessage.onetimemessage.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidOptionalStringLengthClass.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOptionalStringLength {
    String message() default "Invalid string custom validation";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}