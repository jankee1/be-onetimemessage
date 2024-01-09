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

    public MessageDto insert(MessageModel messageModel) throws Exception {
        SecretKey newSecretKey = EncryptionService.generateSecretKey();
        SecretKey secretKeyWithSalt = EncryptionService.getSecretKeyWithSalt(newSecretKey);
        Optional<Boolean> emailSentSuccessfully = null;
        messageModel.setSecretKey(newSecretKey);
        messageModel.setMessageBody(EncryptionService.encrypt(messageModel.getMessageBody(), secretKeyWithSalt));

        this.messageRepository.save(MessageRepository.mapToEntity(messageModel));

        if(!messageModel.getEmailRecipient().isEmpty()) {
            Email email = new Email(messageModel.getId(), messageModel.getEmailRecipient());
            emailSentSuccessfully = Optional.ofNullable(email.send());
        }

        return new MessageDto(Optional.of(messageModel.getId()), null, null, emailSentSuccessfully);
    }

    public Optional<MessageModel> getOne(UUID id) throws Exception {
        Optional<MessageEntity> message = this.messageRepository.findById(id);

        return Optional.ofNullable(message.map(entity -> {
            SecretKey newSecretKey = entity.getSecretKey();
            SecretKey secretKeyWithSalt = EncryptionService.getSecretKeyWithSalt(newSecretKey);
            String messageBody = null;

            try {
                messageBody = EncryptionService.decrypt(entity.getMessageBody(), secretKeyWithSalt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            entity.setMessageBody(messageBody);
//            this.messageRepository.deleteById(entity.getId());
            return MessageRepository.mapToModel(entity);
        }).orElse(null));
    }
}
