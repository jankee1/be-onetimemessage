package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.controller.MessageDto;
import com.example.onetimemessage.onetimemessage.entity.MessageEntity;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import com.example.onetimemessage.onetimemessage.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public ArrayList<MessageDto> sendMessages(ArrayList<MessageModel> messageModels) throws Exception {
        ArrayList<MessageDto> result = new ArrayList();
        for (MessageModel model : messageModels) {
            MessageDto messageSaved = this.insert(model);
            result.add(messageSaved);
        }
        return result;
    }

    public MessageModel getOne(UUID id) throws Exception {
        Optional<MessageEntity> message = this.messageRepository.findById(id);

        return message.map(entity -> {
            SecretKey newSecretKey = entity.getSecretKey();
            SecretKey secretKeyWithSalt = EncryptionService.getSecretKeyWithSalt(newSecretKey);
            String messageBody = null;

            try {
                messageBody = EncryptionService.decrypt(entity.getMessageBody(), secretKeyWithSalt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            entity.setMessageBody(messageBody);
            this.messageRepository.deleteById(id);
            return MessageRepository.mapToModel(entity);
        }).orElse(null);
    }

    private MessageDto insert(MessageModel messageModel) throws Exception {
        SecretKey newSecretKey = EncryptionService.generateSecretKey();
        SecretKey secretKeyWithSalt = EncryptionService.getSecretKeyWithSalt(newSecretKey);

        messageModel.setSecretKey(newSecretKey);
        messageModel.setMessageBody(EncryptionService.encrypt(messageModel.getMessageBody(), secretKeyWithSalt));

        messageModel.setId(UUID.randomUUID());
        Boolean emailSentSuccessfully = null;

        this.messageRepository.save(MessageRepository.mapToEntity(messageModel));

        if (!messageModel.getEmailRecipient().isEmpty()) {
            Email email = new Email(messageModel.getId(), messageModel.getEmailRecipient());
            emailSentSuccessfully = email.send();
        }

        messageModel.setMessageBody(null);
        messageModel.setEmailSentSuccessfully(emailSentSuccessfully);
        return MessageDto.toResponseObject(messageModel);
    }
}
