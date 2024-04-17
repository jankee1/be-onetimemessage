package com.example.onetimemessage.onetimemessage.controller;

import com.example.onetimemessage.onetimemessage.utils.validator.ValidOptionalStringLength;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
}
