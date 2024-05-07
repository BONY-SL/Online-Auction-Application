package com.example.BidZone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id")
    private UserProfile userProfile;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    @Email
    @Column(nullable = false, unique = true)
    private String email;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
