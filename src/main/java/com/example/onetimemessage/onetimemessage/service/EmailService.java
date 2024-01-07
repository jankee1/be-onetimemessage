package com.example.onetimemessage.onetimemessage.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService {
    public void sendEmail(UUID messageId, String email) {
        System.out.println("Sending URL to email");
    }
}
