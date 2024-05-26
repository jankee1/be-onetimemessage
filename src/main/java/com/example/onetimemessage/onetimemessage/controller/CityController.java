package com.example.onetimemessage.onetimemessage.controller;

import com.example.onetimemessage.onetimemessage.model.CityModel;
import com.example.onetimemessage.onetimemessage.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("city")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping()
    public List<CityModel> getCities(@RequestParam("name") String cityName)  {
        return this.cityService.getCityData(cityName);
    }
}
