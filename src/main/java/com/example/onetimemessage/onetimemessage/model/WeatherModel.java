package com.example.onetimemessage.onetimemessage.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WeatherModel {
    int minTemp;
    int maxTemp;
    LocalDateTime date;
}
