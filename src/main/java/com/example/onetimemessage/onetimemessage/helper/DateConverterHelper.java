package com.example.onetimemessage.onetimemessage.helper;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateConverterHelper {
    public LocalDateTime stringToLocaleDateTime(String dateTimeString) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var localDateTime = LocalDateTime.parse(dateTimeString, formatter);

        var localDate = localDateTime.toLocalDate();

        var targetFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        var formattedDateTime = localDate.atStartOfDay().format(targetFormatter);

        return LocalDateTime.parse(formattedDateTime, targetFormatter);
    }
}
