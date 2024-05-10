package com.example.BidZone.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "auction")
public class Auction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action_name;

    @Column(nullable = false)
    private String description;

    @Column
    private String Image;

    @OneToOne(mappedBy = "auction", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private BiddingItem name;

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
        this.action_name = name;
        this.description = description;
    }

    @PrePersist
    public void prePersist() {
        final LocalDateTime localDateTime = LocalDateTime.now();
        if(closingTime == null) {
            closingTime = localDateTime.plusWeeks(1);
        }
        if(createdAt == null) {
            createdAt = localDateTime;
        }
    }

    public boolean getIsClosed() {
        final LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.isAfter(closingTime);
    }
}
