package com.example.onetimemessage.onetimemessage.entity;

import jakarta.persistence.*;
import lombok.*;

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
