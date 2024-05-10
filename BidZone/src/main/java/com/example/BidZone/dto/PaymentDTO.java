
package com.example.BidZone.dto;

import com.example.BidZone.entity.Auction;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class PaymentDTO {
    Long payment_id;
    String auction_name;
    double amount;
    String addresss;
    String pnumber;
    String paymentmethord;
    Date payment_date;
    String username;
    String auctionid;
}

