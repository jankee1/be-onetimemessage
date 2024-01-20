package com.example.onetimemessage.onetimemessage.controller;
import com.example.onetimemessage.onetimemessage.model.MessageModel;
import com.example.onetimemessage.onetimemessage.service.MessageService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
    public MessageDto getOne(@PathVariable UUID id) throws Exception {
        MessageModel model = this.messageService.getOne(id);
        return MessageDto.toResponseObject(model);
    }

    @PostMapping
    public ArrayList<MessageDto> create(@RequestBody @Size(min = 1, max = 5) ArrayList<@Valid MessageDto> dto) throws Exception {
        ArrayList<MessageModel> messagesModels = dto.stream().map(MessageDto::toModel)
                        .sorted(Comparator.comparingInt(MessageModel::getOrder))
                        .collect(Collectors.toCollection(ArrayList::new));
        return this.messageService.sendMessages(messagesModels);
    }
}
