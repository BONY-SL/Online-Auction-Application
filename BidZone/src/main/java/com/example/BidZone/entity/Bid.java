package com.example.BidZone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"auction", "placedBy"})
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "placed_by_id")
    private User placedBy;

    @Column
    private String comment;

    @DecimalMin(value = "1.0", message = "Bid value is too small")
    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime placedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @PrePersist
    public void prePersistBid() {
        if (placedAt == null) {
            placedAt = LocalDateTime.now();
        }
    }
}
