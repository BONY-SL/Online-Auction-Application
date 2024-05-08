package com.example.BidZone.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageRespDTO {

    @NotNull
    private String userName;
    @NotNull
    private String message;
    @NotNull
    private LocalDateTime dateTime;
}
