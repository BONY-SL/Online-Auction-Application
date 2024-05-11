package com.example.BidZone.repostry;

import com.example.BidZone.entity.User;
import com.example.BidZone.entity.UserMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepostory extends CrudRepository<UserMessage, Long> {

    List<UserMessage> findChatMessageBySentByIdAndSentToId(Long sentBy, Long sentTo);
}
