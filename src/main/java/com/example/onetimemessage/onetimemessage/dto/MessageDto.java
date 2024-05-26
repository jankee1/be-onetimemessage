package com.example.onetimemessage.onetimemessage.dto;

import com.example.onetimemessage.onetimemessage.model.CityModel;
import com.example.onetimemessage.onetimemessage.utils.validator.ValidOptionalStringLength;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
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

    @JsonProperty("meetingDate")
    private LocalDateTime meetingDate;

    @JsonProperty("meetingPlace")
    private CityModel meetingPlace;
}
