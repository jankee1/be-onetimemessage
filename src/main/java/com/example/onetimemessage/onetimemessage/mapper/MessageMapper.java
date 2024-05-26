package com.example.onetimemessage.onetimemessage.mapper;

import com.example.onetimemessage.onetimemessage.dto.MessageDto;
import com.example.onetimemessage.onetimemessage.entity.MessageEntity;
import com.example.onetimemessage.onetimemessage.model.CityModel;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import com.example.onetimemessage.onetimemessage.model.WeatherModel;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageMapper {
    private final  MeetingMapper meetingMapper;

    public MessageMapper(MeetingMapper meetingMapper) {
        this.meetingMapper = meetingMapper;
    }

    public MessageModel dtoToModel(MessageDto dto) {
        if(dto == null) {
            return null;
        }
        var model = new MessageModel();

        model.setMessageBody(dto.getMessageBody());
        model.setEmailRecipient(dto.getEmailRecipient());
        model.setOrder(dto.getOrder());
        model.setMeetingDate(dto.getMeetingDate());
        model.setMeetingPlace(dto.getMeetingPlace());
        return model;
    }

    public MessageDto modelToResponseObject(MessageModel model) {
        if(model == null ) {
            return null;
        }
        var dto = new MessageDto();
        dto.setId(model.getId());
        dto.setOrder(model.getOrder());
        dto.setEmailRecipient(model.getEmailRecipient());
        dto.setMessageBody(model.getMessageBody());
        if(Objects.nonNull(model.getEmailSentSuccessfully())) {
            dto.setEmailSentSuccessfully(model.getEmailSentSuccessfully());
        }
        return dto;
    }

    public MessageEntity modelToEntity(MessageModel model) {
        if (model == null) {
            return null;
        }

        var cityModel = model.getMeetingPlace();
        var weatherModel = Optional.ofNullable(cityModel)
                .map(CityModel::getWeatherForecast)
                .flatMap(list -> list.stream().findFirst())
                .orElse(null);
        var meetingEntity = this.meetingMapper.rawToEntity(model.getMeetingDate(), cityModel, weatherModel);

        return new MessageEntity(model.getId(), model.getMessageBody(), model.getEmailRecipient(), model.getSecretKey(), LocalDateTime.now(), meetingEntity);
    }

    public MessageDto entityToResponseObject(MessageEntity entity) {
        if(entity == null) {
            return null;
        }

        var responseObj = new MessageDto();

        responseObj.setId(entity.getId());
        responseObj.setMessageBody(entity.getMessageBody());
        responseObj.setEmailRecipient(entity.getEmailRecipient());

        Optional.ofNullable(entity.getMeeting()).ifPresent(meeting-> {
            var meetingPace = Optional.ofNullable(meeting.getCity()).map(city-> {
                var weatherModel = new WeatherModel();
                weatherModel.setMinTemp(meeting.getMinTemp());
                weatherModel.setMaxTemp(meeting.getMaxTemp());

                var weatherList = new ArrayList<WeatherModel>();
                weatherList.add(weatherModel);

                var cityModel = new CityModel();
                cityModel.setName(city.getCityFullName());
                cityModel.setWeatherForecast(weatherList);

                return cityModel;
            }).orElse(null);

            responseObj.setMeetingDate(meeting.getDate());
            responseObj.setMeetingPlace(meetingPace);
        });

        return responseObj;
    }

    public List<MessageModel> dtosToModels(ArrayList<MessageDto> dtos) {
        return dtos
                .stream()
                .map(this::dtoToModel)
                .sorted(Comparator.comparingInt(MessageModel::getOrder))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
