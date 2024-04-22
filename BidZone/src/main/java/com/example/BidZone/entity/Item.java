package com.example.BidZone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    private String description;

    private double startingPrice;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auction_id")
    private Auction auction;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    private Category category;

    protected Item() {
    }

    public Item(String name, Category category) {
        this.name = name;
        this.category = category;
    }
}
