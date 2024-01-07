package com.example.onetimemessage.onetimemessage.repository;

import com.example.onetimemessage.onetimemessage.entity.MessageEntity;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    static MessageEntity mapToEntity(MessageModel model) {
        MessageEntity entity = new MessageEntity();
        entity.setId(model.getId());
        entity.setMesssageBody(model.getMesssageBody());
        entity.setEmailRecipient(model.getEmailRecipient());
        return entity;
    }

    static MessageModel mapToModel(MessageEntity entity) {
        return new MessageModel(entity.getId(), entity.getMesssageBody(), entity.getEmailRecipient());
    }
}
