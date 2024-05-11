package com.example.BidZone.dto;


import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class MyBidDTO {
    Long id;
    LocalDateTime placedAt;
    Double amount;
    String placedByUsername;
    Long placedById;
    String comment;
    LocalDateTime auctionClosingTime;
    String auctionName;
    Double auctionCurrentHighestBidAmount;
    Long auctionId;
}
