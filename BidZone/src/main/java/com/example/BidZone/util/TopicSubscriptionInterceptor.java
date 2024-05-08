package com.example.BidZone.util;

import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import java.security.Principal;

public class TopicSubscriptionInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            final Principal userPrincipal = headerAccessor.getUser();
            if (userPrincipal == null || !validateSubscriptionAndSend(userPrincipal, headerAccessor.getDestination())) {
                throw new MessagingException("No permission for this topic");
            }
        }
        return message;
    }

    private boolean validateSubscriptionAndSend(final Principal principal, final String destination) {
        if (principal == null || principal.getName() == null || destination == null) {
            return false;
        }

        final String[] paths = destination.split("/");
        final String topicName = paths[paths.length - 1];

        return topicName.equalsIgnoreCase(principal.getName());
    }
}
