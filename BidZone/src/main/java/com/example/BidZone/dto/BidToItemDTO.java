package com.example.BidZone.dto;


import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidToItemDTO {

    private double amount;
    private String comment;
}
