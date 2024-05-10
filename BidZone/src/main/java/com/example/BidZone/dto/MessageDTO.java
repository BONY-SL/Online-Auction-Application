package com.example.BidZone.dto;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Serializable {

    private UserDTO sentBy;
    private UserDTO sentTo;
    private LocalDateTime sentAt;
    private String content;
}
