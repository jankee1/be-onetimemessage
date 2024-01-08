package com.example.onetimemessage.onetimemessage;

import org.springframework.stereotype.Component;

@Component
public class ConfigExample {

    String encryptionSalt;
    public ConfigExample() {
        this.encryptionSalt = "example";
    }
}