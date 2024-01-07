package com.example.onetimemessage.onetimemessage.controller;
import com.example.onetimemessage.onetimemessage.model.MessageModel;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)

public class MessageDto {

    @Length(min = 3, max = 700)
    @JsonProperty("messsageBody")
    final String messsageBody;

//    @Length(max = 255)
    @JsonProperty("emailRecipient")
    Optional<String> emailRecipient;

    static MessageModel toModel(MessageDto dto) {
        return new MessageModel(UUID.randomUUID() ,dto.messsageBody, String.valueOf(dto.emailRecipient));
    }
    static void toResponseObject() {}

}
