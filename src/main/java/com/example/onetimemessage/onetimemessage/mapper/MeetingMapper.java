package com.example.onetimemessage.onetimemessage.mapper;

import com.example.onetimemessage.onetimemessage.entity.MeetingEntity;
import com.example.onetimemessage.onetimemessage.model.CityModel;
import com.example.onetimemessage.onetimemessage.model.WeatherModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MeetingMapper {

    private final CityMapper cityMapper;

    public MeetingMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

    public MeetingEntity rawToEntity(LocalDateTime date, CityModel city, WeatherModel weather) {
        if(date == null) {
            // each meeting must have date
            return null;
        }
        var entity = new MeetingEntity();
        entity.setDate(date);

        var cityEntity = Optional.ofNullable(city).map(this.cityMapper::modelToEntity).orElse(null);
        entity.setCity(cityEntity);

        entity.setMinTemp(Optional.ofNullable(weather).map(WeatherModel::getMinTemp).orElse(null));
        entity.setMaxTemp(Optional.ofNullable(weather).map(WeatherModel::getMaxTemp).orElse(null));

        return entity;
    }
}
