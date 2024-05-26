package com.example.onetimemessage.onetimemessage.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WeatherModel {
    Double minTemp;
    Double maxTemp;
    LocalDateTime date;
}
