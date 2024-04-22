package com.example.BidZone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column
    private String s3ImageURL;

    @OneToOne(mappedBy = "auction", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Item item;

    @Column(nullable = false)
    private LocalDateTime closingTime;


    @ManyToOne(optional = false, targetEntity = User.class)
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "bid_id")
    private Bid currentHighestBid;

    public Auction(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @PrePersist
    public void prePersist() {
        final LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        if(closingTime == null) {
            closingTime = localDateTime.plusWeeks(1);
        }
        if(createdAt == null) {
            createdAt = localDateTime;
        }
    }

    public boolean getIsClosed() {
        final LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        return localDateTime.isAfter(closingTime);
    }
}
