package com.example.onetimemessage.onetimemessage.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class MessageModel {
    UUID id;
    String messageBody;
    SecretKey secretKey;
    String emailRecipient;
    Boolean emailSentSuccessfully;
    int order;
}
