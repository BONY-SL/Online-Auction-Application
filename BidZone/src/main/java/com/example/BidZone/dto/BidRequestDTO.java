package com.example.BidZone.dto;


import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidRequestDTO {
    private Long auctionId;
    private String username;
    private BidToItemDTO bidToItemDTO;

}
