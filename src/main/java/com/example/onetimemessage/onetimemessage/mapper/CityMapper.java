package com.example.onetimemessage.onetimemessage.mapper;

import com.example.onetimemessage.onetimemessage.dto.CityDto;
import com.example.onetimemessage.onetimemessage.entity.CityEntity;
import com.example.onetimemessage.onetimemessage.model.CityModel;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CityMapper {
    public CityModel dtoToModel(CityDto dto) {
        if(dto == null ||  this.isInstanceInValid(dto.getName(), dto.getLon(), dto.getLat())) {
            return null;
        }

        var model = new CityModel();

        model.setLon(dto.getLon());
        model.setLat(dto.getLat());
        model.setName(dto.getName());
        model.setWeatherForecast(dto.getWeatherForecast());

        return model;
    }

    public CityEntity modelToEntity(CityModel model) {
        if(model == null || this.isInstanceInValid(model.getName(), model.getLon(), model.getLat() )) {
            return null;
        }

        var entity = new CityEntity();

        entity.setCityFullName(model.getName());
        entity.setLon(model.getLon());
        entity.setLat(model.getLat());

        return entity;
    }

    public boolean isInstanceInValid(String name, Double lon, Double lat) {
        return Objects.equals(name, null)  || Objects.equals(lon, null) || Objects.equals(lat, null);
    }
}
