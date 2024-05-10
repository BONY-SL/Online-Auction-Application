package com.example.BidZone.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO implements Serializable {
    long id;
    String name;
    String description;
    double startingPrice;
    CategoryDTO category;
    long auctionId;
}
