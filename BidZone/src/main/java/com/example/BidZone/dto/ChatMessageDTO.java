package com.example.BidZone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private String userName;
    private ZonedDateTime sentAt;
    private String message;
}
