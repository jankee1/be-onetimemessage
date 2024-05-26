package com.example.onetimemessage.onetimemessage.dto;

import com.example.onetimemessage.onetimemessage.model.CoordinatesModel;
import com.example.onetimemessage.onetimemessage.model.WeatherModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CityDto extends CoordinatesModel {
    private String name;
    private String addresstype;
    private List<WeatherModel> weatherForecast;

    @JsonProperty("display_name")
    public void setDisplay_name(String name) {
        this.name = name;
    }
}
