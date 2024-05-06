package com.example.BidZone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {

    @NotNull
    private String userName;
    @NotNull
    private String message;

    @NotNull
    private ZonedDateTime dateTime;
}
