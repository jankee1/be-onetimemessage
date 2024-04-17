package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.controller.MessageDto;
import com.example.onetimemessage.onetimemessage.mapper.MessageMapper;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import com.example.onetimemessage.onetimemessage.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    @Autowired
    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    public List<MessageDto> sendMessages(ArrayList<MessageDto> dtos) throws Exception {
        var result = new ArrayList<MessageDto>();
        var messageModels = this.messageMapper.dtosToModels(dtos);
        for (MessageModel model : messageModels) {
            MessageDto messageSaved = this.insert(model);
            result.add(messageSaved);
        }
        return result;
    }

    public MessageDto getOne(UUID id) throws Exception {
        var message = this.messageRepository.findById(id);

        return message.map(entity -> {
            var secretKeyWithSalt = EncryptionService.getSecretKeyWithSalt(entity.getSecretKey());
            String messageBody = null;

            try {
                messageBody = EncryptionService.decrypt(entity.getMessageBody(), secretKeyWithSalt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            entity.setMessageBody(messageBody);
            this.messageRepository.deleteById(id);
            return this.messageMapper.entityToResponseObject(entity);
        }).orElse(null);
    }

    private MessageDto insert(MessageModel messageModel) throws Exception {
        var newSecretKey = EncryptionService.generateSecretKey();
        var secretKeyWithSalt = EncryptionService.getSecretKeyWithSalt(newSecretKey);

        messageModel.setSecretKey(newSecretKey);
        messageModel.setMessageBody(EncryptionService.encrypt(messageModel.getMessageBody(), secretKeyWithSalt));
        messageModel.setId(UUID.randomUUID());

        var emailSentSuccessfully = false;

        this.messageRepository.save(this.messageMapper.modelToEntity(messageModel));

        if (!messageModel.getEmailRecipient().isEmpty()) {
            var email = new Email(messageModel.getId(), messageModel.getEmailRecipient());
            emailSentSuccessfully = email.send();
        }

        messageModel.setMessageBody(null);
        messageModel.setEmailSentSuccessfully(emailSentSuccessfully);

        return this.messageMapper.modelToResponseObject(messageModel);
    }
}
