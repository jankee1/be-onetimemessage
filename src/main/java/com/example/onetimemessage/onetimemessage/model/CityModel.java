package com.example.onetimemessage.onetimemessage.model;
import lombok.Data;

import java.util.List;

@Data
public class CityModel extends CoordinatesModel{
    private String name;
    private List<WeatherModel> weatherForecast;
}
