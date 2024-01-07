package com.example.onetimemessage.onetimemessage.controller;
import com.example.onetimemessage.onetimemessage.entity.MessageEntity;
import com.example.onetimemessage.onetimemessage.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("message")
public class MessageController {

    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{id}")
    public Optional<MessageEntity> getOne(@PathVariable UUID id) {
        return this.messageService.getOne(id);
    }

    @PostMapping
    public String create(@RequestBody MessageDto dto) {
        return this.messageService.insert(MessageDto.toModel(dto));
    }
}
