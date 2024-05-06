package com.example.BidZone.websocketinterface;


import com.example.BidZone.dto.MessageResponseDTO;
import com.example.BidZone.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;
    @MessageMapping("/chat/{channelName}")
    public void chat(final Principal principal, @DestinationVariable final String channelName, final String body) {
        final MessageResponseDTO messageResponseDTO = MessageResponseDTO.builder()
                .userName(principal.getName())
                .message(body)
                .dateTime(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
        simpMessagingTemplate.convertAndSend("/topic/" + channelName, messageResponseDTO);
        chatMessageService.saveChatHistory(principal.getName(), channelName, body);
    }
}
