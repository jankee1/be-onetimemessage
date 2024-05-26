package com.example.onetimemessage.onetimemessage.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigExample {
    private final String messageBodyEncryptionSalt = "example";
    private final String emailLogin = "login";
    private final String emailPassword = "password";
    private final String emailHost = "host";
    private final int emailPort = 587;
    private final int maxBodyLength = 700;
    private final String openWeatherMapApiAccessId = "example";
}