package com.example.onetimemessage.onetimemessage.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigExample {
    private final String MESSAGE_BODY_ENCRYPTION_SALT = "example";
    private final String EMAIL_LOGIN = "login";
    private final String EMAIL_PASSWORD = "password";
    private final String EMAIL_HOST = "host";
    private final int EMAIL_PORT = 587;
    private final int MAX_BODY_LENGTH = 700;
}