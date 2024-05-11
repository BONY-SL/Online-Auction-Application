package com.example.BidZone.util;
import com.example.BidZone.dto.AuctionDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public interface AuctionNotifyInfo {

     AuctionDTO andNewAuctionNotification();

}
