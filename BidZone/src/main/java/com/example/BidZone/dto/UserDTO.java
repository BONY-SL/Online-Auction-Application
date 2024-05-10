package com.example.BidZone.dto;

import com.example.BidZone.entity.User;
import com.example.BidZone.entity.UserProfile;
import lombok.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private UserProfileDTO profile;


    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        if (this.profile != null) {
            user.setUserProfile(this.profile.toEntity());
        } else {
            user.setUserProfile(new UserProfile());
        }
        return user;
    }
}
