package com.example.BidZone.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageUserDTO {

    private String userName;
    private LocalDateTime sentAt;
    private String message;
}
