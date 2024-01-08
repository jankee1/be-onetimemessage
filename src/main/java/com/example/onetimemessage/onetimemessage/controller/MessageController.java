package com.example.onetimemessage.onetimemessage.controller;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import com.example.onetimemessage.onetimemessage.service.MessageService;
import jakarta.validation.Valid;
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
    public Optional<MessageDto> getOne(@PathVariable UUID id) throws Exception {
        Optional<MessageModel> model = this.messageService.getOne(id);
        return MessageDto.toResponseObject(model);
    }

    @PostMapping
    public void create(@Valid @RequestBody MessageDto dto) throws Exception {
        this.messageService.insert(MessageDto.toModel(dto));
    }
}
