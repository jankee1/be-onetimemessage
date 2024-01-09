package com.example.onetimemessage.onetimemessage.model;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.crypto.SecretKey;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Data
public class MessageModel {
    UUID id;
    String messageBody;
    SecretKey secretKey;
    String emailRecipient;
}
