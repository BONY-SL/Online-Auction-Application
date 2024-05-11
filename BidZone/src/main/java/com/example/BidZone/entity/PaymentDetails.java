package com.example.BidZone.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity(name="paymentDetails")
public class PaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payment_id;

    @Column(nullable = false)
    private String auction_name;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String addresss;

    @Column(nullable = false)
    private String pnumber;

    @Column(nullable = false)
    private String paymentmethord;

    @Column(nullable = false)
    private Date payment_date;

    @Column(nullable = false)
    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auction_id", referencedColumnName = "id")
    private Auction auction;

}
