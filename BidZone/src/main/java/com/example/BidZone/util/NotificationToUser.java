package com.example.BidZone.util;
import com.example.BidZone.dto.AuctionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationToUser implements AuctionNotifyInfo{


    @Autowired
    private NewAcutionCreational newAcutionCreational;
    @Override
    public AuctionDTO andNewAuctionNotification() {

        AuctionDTO returnAuction=newAcutionCreational.notifytoUser();
        System.out.println(returnAuction);
        newAcutionCreational.removeAuction();
        return returnAuction;
    }
}
