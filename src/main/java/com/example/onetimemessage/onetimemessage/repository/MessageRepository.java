package com.example.onetimemessage.onetimemessage.repository;

import com.example.onetimemessage.onetimemessage.entity.MessageEntity;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    static MessageEntity mapToEntity(MessageModel model) {
        if (model == null) {
            return null;
        }
        return new MessageEntity(model.getId(), model.getMessageBody(), model.getEmailRecipient(), model.getSecretKey(), LocalDateTime.now());
    }

    static MessageModel mapToModel(MessageEntity entity) {
        if(entity == null) {
            return null;
        }
        MessageModel model = new MessageModel();
        model.setId(entity.getId());
        model.setMessageBody(entity.getMessageBody());
        model.setEmailRecipient(entity.getEmailRecipient());
        return model;
    }
}
