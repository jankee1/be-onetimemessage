package com.example.onetimemessage.onetimemessage.controller;

import com.example.onetimemessage.onetimemessage.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Validated
@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{id}")
    public MessageDto getOne(@PathVariable UUID id) throws Exception {
        return this.messageService.getOne(id);
    }

    @PostMapping
    public List<MessageDto> create(@RequestBody @Size(min = 1, max = 15) ArrayList<@Valid MessageDto> dtos) throws Exception {
        return this.messageService.sendMessages(dtos);
    }
}
