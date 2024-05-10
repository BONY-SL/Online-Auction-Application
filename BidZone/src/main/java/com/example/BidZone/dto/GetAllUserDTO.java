package com.example.BidZone.dto;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUserDTO {

    private Long id;
    private UserProfileDTO profile;

}
