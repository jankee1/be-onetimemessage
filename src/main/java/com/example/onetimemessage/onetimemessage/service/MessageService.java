package com.example.onetimemessage.onetimemessage.service;

import com.example.onetimemessage.onetimemessage.entity.MessageEntity;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import com.example.onetimemessage.onetimemessage.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final int ONE_CHUNK_LENGTH = 10;
    private final String SEPARATOR = ",";

    private final EmailService emailService;
    private final MessageRepository messageRepository;
    @Autowired
    public MessageService(EmailService emailService, MessageRepository messageRepository) {
        this.emailService = emailService;
        this.messageRepository = messageRepository;
    }

    public String insert(MessageModel messageModel) {
        messageModel.setMesssageBody(this.splitIntoChunks(messageModel.getMesssageBody()));
        String email = messageModel.getEmailRecipient();
        if(email != null && !email.isEmpty()) {
            this.emailService.sendEmail(messageModel.getId(), messageModel.getEmailRecipient());
        }
        this.messageRepository.save(MessageRepository.mapToEntity(messageModel));
        System.out.println(messageModel);
        return "Inserting message";
    }

    public String getOne(String id) {
        return "hello world " + id;
    }

    private String splitIntoChunks(String str) {
        List<String> strSplited = new ArrayList<>();
        int iterationCounter = (int)Math.round(Math.ceil(Float.parseFloat(String.valueOf(str.length())) / this.ONE_CHUNK_LENGTH));

        for(int i = 0; i < iterationCounter ; i++) {
            String chunk;
            if(str.length() <= this.ONE_CHUNK_LENGTH) {
                chunk = str;
            } else {
                chunk = str.substring(0, this.ONE_CHUNK_LENGTH);
                str = str.substring(this.ONE_CHUNK_LENGTH);
            }
            strSplited.add(this.hashMessage(chunk));
        }

        return strSplited.stream().collect(Collectors.joining(this.SEPARATOR));
    }

    private String hashMessage(String message) {
        return message;
    }
}
