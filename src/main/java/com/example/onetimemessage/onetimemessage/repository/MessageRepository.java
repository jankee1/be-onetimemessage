package com.example.onetimemessage.onetimemessage.repository;

import com.example.onetimemessage.onetimemessage.entity.MessageEntity;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    static MessageEntity mapToEntity(MessageModel model) {
        MessageEntity entity = new MessageEntity(model.getId(), model.getMesssageBody(), model.getEmailRecipient(), model.getSecretKey(), LocalDateTime.now());
        return entity;
    }

    static MessageModel mapToModel(MessageEntity entity) {
        return new MessageModel(entity.getId(), entity.getMesssageBody(), null,entity.getEmailRecipient());
    }
}
