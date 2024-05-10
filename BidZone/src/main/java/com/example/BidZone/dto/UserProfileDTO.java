package com.example.BidZone.dto;


import com.example.BidZone.entity.UserProfile;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO implements Serializable {

    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String description;
    private String profilePictureURL;

    public UserProfile toEntity() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(this.id);
        userProfile.setFirstName(this.firstName);
        userProfile.setLastName(this.lastName);
        userProfile.setDescription(this.description);
        userProfile.setProfilePictureS3URL(this.profilePictureURL);
        return userProfile;
    }
}
