package com.example.onetimemessage.onetimemessage.model;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class MessageModel {
    UUID id;
    String messsageBody;
    String emailRecipient;

}
