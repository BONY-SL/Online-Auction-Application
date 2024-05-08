package com.example.BidZone.repostry;

import com.example.BidZone.entity.UserMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepostory extends CrudRepository<UserMessage, Long> {
}
