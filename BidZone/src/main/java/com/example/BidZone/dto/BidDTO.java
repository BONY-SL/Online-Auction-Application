package com.example.BidZone.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class BidDTO {
    Long id;
    LocalDateTime placedAt;
    Double amount;
    String placedByUsername;
    Long placedById;
    String comment;
}