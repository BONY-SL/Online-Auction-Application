package com.example.BidZone.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    long id;
    String name;
    String description;
    double startingPrice;
    CategoryDTO category;
    long auctionId;
}
