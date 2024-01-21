package com.example.onetimemessage.onetimemessage.controller;

import com.example.onetimemessage.onetimemessage.model.MessageModel;
import com.example.onetimemessage.onetimemessage.utils.validator.ValidOptionalStringLength;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDto {

    @JsonProperty("id")
    private UUID id;

    @ValidOptionalStringLength
    @JsonProperty("messageBody")
    private String messageBody;

    @JsonProperty("emailRecipient")
    @Email
    private String emailRecipient;

    @JsonProperty("emailSentSuccessfully")
    private boolean emailSentSuccessfully;

    @JsonProperty("order")
    private int order;

    public static MessageModel toModel(MessageDto dto) {
        if(dto == null) {
            return null;
        }
        MessageModel model = new MessageModel();
        model.setMessageBody(dto.messageBody);
        model.setEmailRecipient(dto.emailRecipient);
        model.setOrder(dto.order);
        return model;
    }
    public static MessageDto toResponseObject(MessageModel model) {
        if(model == null ) {
            return null;
        }
        MessageDto dto = new MessageDto();
        dto.setId(model.getId());
        dto.setOrder(model.getOrder());
        dto.setEmailRecipient(model.getEmailRecipient());
        dto.setMessageBody(model.getMessageBody());
        if(Objects.nonNull(model.getEmailSentSuccessfully())) {
            dto.setEmailSentSuccessfully(model.getEmailSentSuccessfully());
        }
        return dto;
    }
}
