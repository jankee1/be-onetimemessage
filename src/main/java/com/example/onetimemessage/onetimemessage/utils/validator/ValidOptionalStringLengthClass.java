package com.example.onetimemessage.onetimemessage.utils.validator;

import com.example.onetimemessage.onetimemessage.Config;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;
public class ValidOptionalStringLengthClass implements ConstraintValidator<ValidOptionalStringLength, Optional<String>> {
    private static final Config CONFIG = new Config();

    @Override
    public boolean isValid(Optional<String> optionalString, ConstraintValidatorContext context) {
        return Optional.ofNullable(optionalString).isPresent() && optionalString.map(s -> s.length() <= CONFIG.getMAX_BODY_LENGTH()).orElse(true);
    }
}
