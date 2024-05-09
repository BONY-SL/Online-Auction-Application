package com.example.BidZone.repostry;

import com.example.BidZone.dto.MessageDTO;
import com.example.BidZone.entity.User;
import org.springframework.stereotype.Service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;



@Service
public interface ChatRepoService extends Remote {

    void sendMessage(MessageDTO message) throws RemoteException;

    MessageDTO responseMessage() throws RemoteException;

    List<MessageDTO> retrieveMessages(User sentBy, User sentTo) throws RemoteException;
}
