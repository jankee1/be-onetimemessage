package com.example.onetimemessage.onetimemessage.controller;
import com.example.onetimemessage.onetimemessage.model.MessageModel;

import com.example.onetimemessage.onetimemessage.utils.validator.ValidOptionalStringLength;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString

public class MessageDto {

    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Optional<UUID> id;

    @ValidOptionalStringLength
    @JsonProperty("messsageBody")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Optional<String> messageBody;

    @JsonProperty("emailRecipient")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Optional<String> emailRecipient;

    @JsonProperty("emailSentSuccessfully")
    Optional<Boolean> emailSentSuccessfully;

    static MessageModel toModel(MessageDto dto) {
        return new MessageModel(UUID.randomUUID() , dto.messageBody.orElse(""), null, dto.emailRecipient.orElse(null));
    }
    static Optional<MessageDto> toResponseObject(Optional<MessageModel> givenModel) {
        return givenModel.map(model -> {
            return new MessageDto(Optional.ofNullable(model.getId()), Optional.ofNullable(model.getMessageBody()), null, null);
        } );
    }
}
