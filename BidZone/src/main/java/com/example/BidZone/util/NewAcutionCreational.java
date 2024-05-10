package com.example.BidZone.util;

import com.example.BidZone.dto.AuctionDTO;
import org.springframework.stereotype.Service;


@Service
public interface NewAcutionCreational {

    void addnewAuction(AuctionDTO auctionDTO);

    void removeAuction();

    AuctionDTO notifytoUser();
}
