package com.example.onetimemessage.onetimemessage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDto {
    private Double tempKelvin;
    private LocalDateTime date;

    @JsonProperty("temp")
    public void setTemp(Double tempKelvin) {
        this.tempKelvin = tempKelvin;
    }
}
