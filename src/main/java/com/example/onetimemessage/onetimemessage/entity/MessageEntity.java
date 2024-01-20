package com.example.onetimemessage.onetimemessage.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class MessageEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String messageBody;
    private String emailRecipient;

    @Column(nullable = false)
    private SecretKey secretKey;

    @CreatedDate
    private LocalDateTime createdAt;
}
