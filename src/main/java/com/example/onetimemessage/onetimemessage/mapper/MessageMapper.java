package com.example.onetimemessage.onetimemessage.mapper;

import com.example.onetimemessage.onetimemessage.controller.MessageDto;
import com.example.onetimemessage.onetimemessage.entity.MessageEntity;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MessageMapper {

    public MessageModel dtoToModel(MessageDto dto) {
        if(dto == null) {
            return null;
        }
        var model = new MessageModel();
        model.setMessageBody(dto.getMessageBody());
        model.setEmailRecipient(dto.getEmailRecipient());
        model.setOrder(dto.getOrder());
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
        return new MessageEntity(model.getId(), model.getMessageBody(), model.getEmailRecipient(), model.getSecretKey(), LocalDateTime.now());
    }

    public MessageDto entityToResponseObject(MessageEntity entity) {
        if(entity == null) {
            return null;
        }
        var responseObj = new MessageDto();
        responseObj.setId(entity.getId());
        responseObj.setMessageBody(entity.getMessageBody());
        responseObj.setEmailRecipient(entity.getEmailRecipient());
        return responseObj;
    }

    public List<MessageModel> dtosToModels(ArrayList<MessageDto> dtos) {
        return dtos
                .stream()
                .map(dtoObj-> {
                    MessageMapper mapper = new MessageMapper();
                    return mapper.dtoToModel(dtoObj);
                })
                .sorted(Comparator.comparingInt(MessageModel::getOrder))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
