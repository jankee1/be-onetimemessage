package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.entity.MessageEntity;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import com.example.onetimemessage.onetimemessage.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final EmailService emailService;
    private final MessageRepository messageRepository;
    @Autowired
    public MessageService(EmailService emailService, MessageRepository messageRepository) {
        this.emailService = emailService;
        this.messageRepository = messageRepository;
    }

    public void insert(MessageModel messageModel) throws Exception {
        SecretKey newSecretKey = EncryptionService.generateSecretKey();
        SecretKey secretKeyWithSalt = EncryptionService.getSecretKeyWithSalt(newSecretKey);

        messageModel.setSecretKey(newSecretKey);
        messageModel.setMesssageBody(EncryptionService.encrypt(messageModel.getMesssageBody(), secretKeyWithSalt));

        String email = messageModel.getEmailRecipient();
        if(!email.isEmpty()) {
            this.emailService.sendEmail(messageModel.getId(), messageModel.getEmailRecipient());
        }
        this.messageRepository.save(MessageRepository.mapToEntity(messageModel));
    }

    public Optional<MessageModel> getOne(UUID id) throws Exception {
        Optional<MessageEntity> message = this.messageRepository.findById(id);

        return Optional.ofNullable(message.map(entity -> {
            SecretKey newSecretKey = entity.getSecretKey();
            SecretKey secretKeyWithSalt = EncryptionService.getSecretKeyWithSalt(newSecretKey);
            String messageBody = null;

            try {
                messageBody = EncryptionService.decrypt(entity.getMesssageBody(), secretKeyWithSalt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            entity.setMesssageBody(messageBody);
//            this.messageRepository.deleteById(entity.getId());
            return MessageRepository.mapToModel(entity);
        }).orElse(null));
    }
}
