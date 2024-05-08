package com.example.BidZone.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String description;
    private String profilePictureURL;
}
