package com.example.onetimemessage.onetimemessage.controller;
import com.example.onetimemessage.onetimemessage.model.MessageModel;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)

public class MessageDto {

    @NotNull
    @Length(min = 3, max = 700)
    @JsonProperty("messsageBody")
    final String messsageBody;

    @JsonProperty("emailRecipient")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Optional<String> emailRecipient;

    static MessageModel toModel(MessageDto dto) {
        return new MessageModel(UUID.randomUUID() ,dto.messsageBody, null, String.valueOf(dto.emailRecipient));
    }
    static Optional<MessageDto> toResponseObject(Optional<MessageModel> givenModel) {
        return givenModel.map(model -> {
            return new MessageDto(model.getMesssageBody(), null);
        } );
    }
}
