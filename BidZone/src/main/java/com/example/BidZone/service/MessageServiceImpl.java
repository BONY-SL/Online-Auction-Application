package com.example.BidZone.service;
import com.example.BidZone.dto.MessageDTO;
import com.example.BidZone.entity.User;
import com.example.BidZone.entity.UserMessage;
import com.example.BidZone.repostry.ChatRepoService;
import com.example.BidZone.repostry.MessageRepostory;
import com.example.BidZone.repostry.UserRepository;
import com.example.BidZone.util.CommonAppExceptions;
import com.example.BidZone.util.RealTimeMessageManage;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageServiceImpl extends UnicastRemoteObject implements ChatRepoService {

    private final MessageRepostory messageRepository;
    private final UserRepository userRepository;
    private final RealTimeMessageManage realTimeMessageManage;

    public MessageServiceImpl(MessageRepostory messageRepository, UserRepository userRepository, RealTimeMessageManage realTimeMessageManage) throws RemoteException {
        super();
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.realTimeMessageManage = realTimeMessageManage;
    }

    @Override
    public void sendMessage(MessageDTO messageDTO) throws RemoteException {
        try {
            User userSendBy = userRepository.findByUsername(messageDTO.getSentBy().getUsername())
                    .orElseThrow(() -> new CommonAppExceptions("User Not Found", HttpStatus.NOT_FOUND));

            User userSendTo = userRepository.findById(messageDTO.getSentTo().getId())
                    .orElseThrow(() -> new CommonAppExceptions("User Not Found", HttpStatus.NOT_FOUND));

            UserMessage message = new UserMessage();
            message.setSentBy(userSendBy);
            message.setSentTo(userSendTo);
            message.setContent(messageDTO.getContent());
            message.setSentAt(LocalDateTime.now());


            messageRepository.save(message);
            realTimeMessageManage.serializeMesageDTOInventory(messageDTO);
            MessageDTO messageDTO1=realTimeMessageManage.deserializeMesageDTOInventory("message.ser");
            System.out.println("Sent By: "+message.getSentBy().getId());
            System.out.println("Sent By: "+message.getSentBy().getUsername());
            System.out.println("Sent To: "+message.getSentTo().getId());
            System.out.println("Sent To: "+message.getSentTo().getUsername());
            System.out.println("Sent At: "+message.getSentAt());
            System.out.println("Content: "+message.getContent());
        } catch (Exception e) {
            throw new RemoteException("Error during message send", e);
        }
    }

    @Override
    @Transactional
    public MessageDTO getMessageResponse(Long id, String username) throws RemoteException, CommonAppExceptions {
        MessageDTO messageDTO = realTimeMessageManage.deserializeMesageDTOInventory("message.ser");

        User SndBy = userRepository.findById(id)
                .orElseThrow(() -> new CommonAppExceptions("User Not Found", HttpStatus.NOT_FOUND));
        Hibernate.initialize(SndBy.getUserProfile());
        User sendToUser = userRepository.findByUsernameWithProfile(username)
                .orElseThrow(() -> new CommonAppExceptions("User Not Found", HttpStatus.NOT_FOUND));
        Hibernate.initialize(sendToUser.getUserProfile());

        messageDTO.getSentBy().setId(sendToUser.getId());
        messageDTO.setSentTo(SndBy.toDTO());

        System.out.println(sendToUser.getUserProfile().toDTO());

        if (messageDTO.getSentTo().getId().equals(id) && messageDTO.getSentBy().getId().equals(sendToUser.getId())) {
            System.out.println(1);
            return messageDTO;
        } else {
            throw new CommonAppExceptions("Message does not match user criteria", HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public List<MessageDTO> retrieveMessages(User sentBy, User sentTo) throws RemoteException {
        List<UserMessage> messages = messageRepository.findChatMessageBySentByAndSentTo(sentBy, sentTo);
        return messages.stream().map(msg -> new MessageDTO(
                msg.getSentBy().toDTO(),
                msg.getSentTo().toDTO(),
                msg.getSentAt(),
                msg.getContent()
        )).collect(Collectors.toList());
    }

}
