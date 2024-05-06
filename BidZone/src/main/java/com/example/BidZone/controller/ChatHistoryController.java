package com.example.BidZone.controller;

import com.example.BidZone.dto.PaginatedChatMessagesDTO;
import com.example.BidZone.dto.UserDTO;
import com.example.BidZone.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
public class ChatHistoryController {

    @Autowired
    private ChatMessageService chatMessageService;


    @GetMapping("/chatHistory/{sentTo}")
    public PaginatedChatMessagesDTO getChatHistory(final Principal principal,
                                                   @PathVariable String sentTo,
                                                   @RequestParam(defaultValue = "0") int pageNumber) {
        try {
            return chatMessageService.getChatHistory(principal.getName(), sentTo, pageNumber);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
    }

    @GetMapping("/chatHistoryUsers")
    public List<UserDTO> getChatHistoryUsers(final Principal principal) {
        return chatMessageService.getUsersWithChatHistory(principal.getName());
    }
}
