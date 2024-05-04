package com.example.BidZone.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserProfileUpdateDTO {

    private String firstName;
    private String lastName;
    private String description;
}
