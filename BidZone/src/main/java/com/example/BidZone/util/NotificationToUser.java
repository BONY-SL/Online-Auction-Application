package com.example.BidZone.util;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationToUser implements AuctionNotifyInfo{
    @Override
    public String andNewAuctionNotification() {
        return "Add New Listing";
    }
}
