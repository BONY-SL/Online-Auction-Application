package com.example.BidZone.repostry;

import com.example.BidZone.dto.MessageDTO;
import com.example.BidZone.entity.User;
import com.example.BidZone.util.CommonAppExceptions;
import org.springframework.stereotype.Service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;



@Service
public interface ChatRepoService extends Remote {

    void sendMessage(MessageDTO message) throws RemoteException;

    MessageDTO getMessageResponse(Long id,String username) throws RemoteException, CommonAppExceptions;

    List<MessageDTO> retrieveMessages(User sentBy, User sentTo) throws RemoteException;
}
