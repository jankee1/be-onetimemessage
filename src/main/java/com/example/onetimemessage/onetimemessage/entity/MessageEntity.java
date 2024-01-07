package com.example.onetimemessage.onetimemessage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class MessageEntity {
    @Id
    private UUID id;

    private String messsageBody;
    private String emailRecipient;
}
