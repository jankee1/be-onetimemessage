package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.config.Config;
import com.example.onetimemessage.onetimemessage.dto.WeatherDto;
import com.example.onetimemessage.onetimemessage.mapper.WeatherMapper;
import com.example.onetimemessage.onetimemessage.model.WeatherModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class WeatherService {

    private final WebClient webClient;
    private final WeatherMapper weatherMapper;
    private final String baseUrl = "https://api.openweathermap.org/data/2.5/";
    private final Config config;

    public WeatherService(WeatherMapper weatherMapper, Config config) {
        this.weatherMapper = weatherMapper;
        this.config = config;
        this.webClient = WebClient.create();
    }

    public List<WeatherModel> getWeather(Double lon, Double lat) {
        var builder = UriComponentsBuilder
                .fromUriString(this.baseUrl)
                .path("forecast")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", this.config.getOpenWeatherMapApiAccessId());

        return this.webClient
                .get()
                .uri(builder.buildAndExpand().toUri())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    var dtos = this.weatherMapper.rawStringToDtos(response);
                    return this.filterTempByDay(dtos);
                })
                .onErrorResume( error -> {
                    System.out.println(error.getMessage());
                    return Mono.empty();
                })
                .block();
    }

    private List<WeatherModel> filterTempByDay(List<WeatherDto> dtos) {
        var datesUnique = new HashSet<LocalDateTime>();
        var models = new ArrayList<WeatherModel>();

        dtos.forEach(dto->datesUnique.add(dto.getDate()));
        datesUnique.forEach(uniqueDate-> {
            var filtered = dtos
                    .stream()
                    .filter(dto -> dto.getDate().equals(uniqueDate))
                    .toList();

            var maxTemp = filtered.stream().mapToDouble(WeatherDto::getTempKelvin).max().orElse(0.0);
            var minTemp = filtered.stream().mapToDouble(WeatherDto::getTempKelvin).min().orElse(0.0);

            models.add(this.weatherMapper.rawDataToModel(minTemp, maxTemp, uniqueDate));
        });
        return models;
    }
}
