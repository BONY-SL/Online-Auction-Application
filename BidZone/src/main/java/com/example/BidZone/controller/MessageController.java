package com.example.BidZone.controller;


import com.example.BidZone.dto.MessageDTO;
import com.example.BidZone.repostry.ChatRepoService;
import com.example.BidZone.util.CommonAppExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
@Controller
@DependsOn("rmiConfig")
public class MessageController {

    @Autowired
    private ChatRepoService chatService;

    @PostMapping("/messages/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO message) throws CommonAppExceptions {
        System.out.println("Attempting to send message: " + message);
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            Registry registry = LocateRegistry.getRegistry(host,4200);

            chatService= (ChatRepoService) registry.lookup("ChatService");
            chatService.sendMessage(message);
        } catch (Exception e) {
            System.out.println("Error during message send: " + e.getMessage());
            throw new CommonAppExceptions("Not Send", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(message.getContent());
    }

    @GetMapping("/messages/response")
    public MessageDTO response() throws RemoteException {
        return chatService.responseMessage();
    }

}