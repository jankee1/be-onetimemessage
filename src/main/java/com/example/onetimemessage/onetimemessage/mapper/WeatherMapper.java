package com.example.onetimemessage.onetimemessage.mapper;

import com.example.onetimemessage.onetimemessage.dto.WeatherDto;
import com.example.onetimemessage.onetimemessage.helper.DateConverterHelper;
import com.example.onetimemessage.onetimemessage.model.WeatherModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherMapper {

    private final DateConverterHelper dateConverter;

    public WeatherMapper(DateConverterHelper dateConverter) {
        this.dateConverter = dateConverter;
    }

    public List<WeatherDto> rawStringToDtos(String raw) {
        var dtos = new ArrayList<WeatherDto>();

        try {
            var objectMapper = new ObjectMapper();
            var jsonNode = objectMapper.readTree(raw);
            var listNodes = jsonNode.get("list");

            for (JsonNode node : listNodes) {
                var main = node.get("main");
                var dto = objectMapper.treeToValue(main, WeatherDto.class);
                var dataTimeStr = node.get("dt_txt").asText();
                var dateTime = this.dateConverter.stringToLocaleDateTime(dataTimeStr);
                dto.setDate(dateTime);
                dtos.add(dto);
            }

        } catch (IOException e) {
            System.err.println("Error occurred while parsing JSON: " + e.getMessage());
        }

        return dtos;
    }

    public WeatherModel rawDataToModel(int minTemp, int maxTemp, LocalDateTime date) {
        var model = new WeatherModel();

        model.setMinTemp(minTemp);
        model.setMaxTemp(maxTemp);
        model.setDate(date);

        return model;
    }
}
