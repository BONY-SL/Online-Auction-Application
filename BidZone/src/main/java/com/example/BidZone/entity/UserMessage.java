package com.example.BidZone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_message")
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, targetEntity = User.class)
    private User sentBy;

    @ManyToOne(optional = false, targetEntity = User.class)
    private User sentTo;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false, length = 1000)
    private String content;

    @PrePersist
    public void prePersist() {
        if(sentAt == null) {
            sentAt = LocalDateTime.now();
        }
    }
}
