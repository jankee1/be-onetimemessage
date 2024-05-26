package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.dto.CityDto;
import com.example.onetimemessage.onetimemessage.mapper.CityMapper;
import com.example.onetimemessage.onetimemessage.model.CityModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CityService {
    private final WeatherService weatherService;
    private final CityMapper cityMapper;
    private final WebClient webClient;
    private final String forbiddenAddressType = "administrative";
    private final String baseUrl = "https://nominatim.openstreetmap.org/";
    public  CityService(WeatherService weatherService, CityMapper cityMapper) {
        this.weatherService = weatherService;
        this.cityMapper = cityMapper;
        this.webClient = WebClient.create();
    }

    public List<CityModel> getCityData(String cityName) {
        var empty = new ArrayList<CityModel>();

        if(cityName.length() == 0) {
            return empty;
        }

        log.info("Requesting city name {}", cityName);

        var builder = UriComponentsBuilder
                .fromUriString(this.baseUrl)
                .path("search")
                .queryParam("city", cityName)
                .queryParam("format", "jsonv2");

        var dtos = this.webClient
                .get()
                .uri(builder.buildAndExpand().toUri())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CityDto>>() {})
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.empty();
                })
                .block();

        log.info("Results found: {}", dtos.size());

        return Optional.ofNullable(dtos).map(items ->
                    items.stream()
                    .filter(dto -> !dto.getAddresstype().equals(this.forbiddenAddressType))
                    .map(dto -> {
                        log.info("Requesting weather details for {}", dto.getName());

                        var model = this.cityMapper.dtoToModel(dto);
                        var weatherForecast = this.weatherService.getWeather(dto.getLon(), dto.getLat());
                        model.setWeatherForecast(weatherForecast);

                        return model;
                }).toList())
                .orElse(empty);
    }
}
