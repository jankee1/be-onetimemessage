package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.dto.MessageDto;
import com.example.onetimemessage.onetimemessage.entity.MessageEntity;
import com.example.onetimemessage.onetimemessage.mapper.MessageMapper;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import com.example.onetimemessage.onetimemessage.repository.CityRepository;
import com.example.onetimemessage.onetimemessage.repository.MeetingRepository;
import com.example.onetimemessage.onetimemessage.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final CityRepository cityRepository;
    private final MeetingRepository meetingRepository;
    private final MessageMapper messageMapper;
    private final EncryptionService encryptionService;
    private final EmailService emailService;
    @Autowired
    public MessageService(MessageRepository messageRepository, CityRepository cityRepository, MeetingRepository meetingRepository,
                          MessageMapper messageMapper, EncryptionService encryptionService, EmailService emailService) {
        this.messageRepository = messageRepository;
        this.cityRepository = cityRepository;
        this.meetingRepository = meetingRepository;
        this.messageMapper = messageMapper;
        this.encryptionService = encryptionService;
        this.emailService = emailService;
    }

    public List<MessageDto> sendMessages(ArrayList<MessageDto> dtos) throws Exception {
        log.info("Request to create new messages. Messages count: ", dtos.size());

        var result = new ArrayList<MessageDto>();
        var messageModels = this.messageMapper.dtosToModels(dtos);

        for (MessageModel model : messageModels) {
            MessageDto messageSaved = this.insert(model);
            result.add(messageSaved);
        }

        log.info("Message saved with nested properties");

        return result;
    }

    public MessageDto getOne(UUID id) throws Exception {
        log.info("Requesting message {}", id);

        return this.messageRepository
                .findById(id)
                .map(entity -> {
            log.info("Message found");

            var secretKeyWithSalt = this.encryptionService.getSecretKeyWithSalt(entity.getSecretKey());
            String messageBody = null;

            try {
                log.info("Encrypting message");
                messageBody = this.encryptionService.decrypt(entity.getMessageBody(), secretKeyWithSalt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            log.info("Returning message");

            entity.setMessageBody(messageBody);
            this.messageRepository.deleteById(id);

            return this.messageMapper.entityToResponseObject(entity);
        }).orElse(null);
    }

    private MessageDto insert(MessageModel messageModel) throws Exception {
        var newSecretKey = this.encryptionService.generateSecretKey();
        var secretKeyWithSalt = this.encryptionService.getSecretKeyWithSalt(newSecretKey);

        messageModel.setSecretKey(newSecretKey);
        messageModel.setMessageBody(this.encryptionService.encrypt(messageModel.getMessageBody(), secretKeyWithSalt));

        var emailSentSuccessfully = false;

        var id = this.saveNested(this.messageMapper.modelToEntity(messageModel));
        messageModel.setId(id);

        if (!messageModel.getEmailRecipient().isEmpty()) {
            var emailAddress = messageModel.getEmailRecipient();
            log.info("Sending email with notification {}", emailAddress);
            emailSentSuccessfully = this.emailService.send(messageModel.getId(), emailAddress);
        }

        messageModel.setMessageBody(null);
        messageModel.setEmailSentSuccessfully(emailSentSuccessfully);

        return this.messageMapper.modelToResponseObject(messageModel);
    }

    @Transactional
    private UUID saveNested(MessageEntity message) {
        log.info("Saving in transaction");

        var meeting = message.getMeeting();

        if (!Objects.equals(meeting, null)) {
            Optional.ofNullable(meeting.getCity()).ifPresent(city->{
                log.info("Saving city");
                this.cityRepository.save(city);
            });

            log.info("Saving meeting");
            this.meetingRepository.save(meeting);
        }

        log.info("Saving message");
        var entity = this.messageRepository.save(message);
        log.info("Transaction completed");

        return entity.getId();
    }
}
