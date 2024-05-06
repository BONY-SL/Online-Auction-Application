package com.example.BidZone.service;


import ch.qos.logback.core.joran.action.AppenderAction;
import com.example.BidZone.dto.ChatMessageDTO;
import com.example.BidZone.dto.PaginatedChatMessagesDTO;
import com.example.BidZone.dto.UserDTO;
import com.example.BidZone.entity.ChatMessage;
import com.example.BidZone.entity.User;
import com.example.BidZone.repostry.ChatMessageRepository;
import com.example.BidZone.repostry.UserRepository;
import com.example.BidZone.util.AppExceptions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    public PaginatedChatMessagesDTO getChatHistory(final String sentBy,final String sentTo,final int pageNumber){
        AppenderAction appenderAction = new AppenderAction();
        final User sentByUser=userRepository.findByUsername(sentBy).orElseThrow(() -> new AppExceptions("Unknown user", HttpStatus.NOT_FOUND));
        final Optional<User> sendToUser = Optional.ofNullable(userRepository.findByUsername(sentTo).orElseThrow(() -> new AppExceptions("Unknown user", HttpStatus.NOT_FOUND)));;
        if (sendToUser.isEmpty()) {
        }
        final PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        final Page<ChatMessage> chatMessages = chatMessageRepository.findChatMessageBySentByAndSentToOrderBySentAtDesc(sentByUser,
                sendToUser.get(), pageRequest);

        final List<ChatMessageDTO> messages = chatMessages.stream().map(chatMessage ->
                ChatMessageDTO.builder().message(chatMessage.getContent())
                        .sentAt(chatMessage.getSentAt().atZone(ZoneId.of("UTC")))
                        .userName(chatMessage.getSentBy().getUsername()).build()).toList();
        return PaginatedChatMessagesDTO.builder().numPages(chatMessages.getTotalPages()).chatMessages(messages).build();
    }

    public void saveChatHistory(final String sentBy, final String sentTo, final String content)  {
        final User sentByUser = userRepository.findByUsername(sentBy).orElseThrow(() -> new AppExceptions("Unknown user", HttpStatus.NOT_FOUND));;
        final Optional<User> sendToUser = Optional.ofNullable(userRepository.findByUsername(sentTo)).orElseThrow(() -> new AppExceptions("Unknown user", HttpStatus.NOT_FOUND));;
        if (sendToUser.isEmpty()) {
        }
        final ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(content);
        chatMessage.setSentBy(sentByUser);
        chatMessage.setSentTo(sendToUser.get());
        chatMessageRepository.save(chatMessage);
    }

    public List<UserDTO> getUsersWithChatHistory(final String userName) {
        final User user = userRepository.findByUsername(userName).orElseThrow(() -> new AppExceptions("Unknown user", HttpStatus.NOT_FOUND));;
        final List<Long> userIds = chatMessageRepository.getDistinctUsersBySentByOrSentAt(user.getId());
        final List<User> users = userRepository.findAllById(userIds);
        return users.stream().map(userEntity -> modelMapper.map(userEntity, UserDTO.class)).collect(Collectors.toList());
    }


}
