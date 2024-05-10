package com.example.BidZone.entity;

import com.example.BidZone.dto.UserProfileDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_profile")
public class UserProfile implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private String description;

    @Column
    private String profilePictureS3URL;



    public UserProfileDTO toDTO() {
        return new UserProfileDTO(id, firstName, lastName, description, profilePictureS3URL);
    }
}
