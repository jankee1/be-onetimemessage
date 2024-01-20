package com.example.onetimemessage.onetimemessage.utils.validator;

import com.example.onetimemessage.onetimemessage.Config;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class ValidOptionalStringLengthClass implements ConstraintValidator<ValidOptionalStringLength, String> {
    private static final Config CONFIG = new Config();
    @Override
    public boolean isValid(String optionalString, ConstraintValidatorContext context) {
        return Objects.nonNull(optionalString) && optionalString.length() <= CONFIG.getMAX_BODY_LENGTH();
    }
}
