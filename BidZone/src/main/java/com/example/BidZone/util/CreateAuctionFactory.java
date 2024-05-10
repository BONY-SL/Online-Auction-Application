package com.example.BidZone.util;
import com.example.BidZone.dto.AuctionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class CreateAuctionFactory implements NewAcutionCreational{

    @Autowired
    private AuctionManage auctionManage;


    @Override
    public void addnewAuction(AuctionDTO auctionDTO) {

        auctionManage.serializeAuctionInventory(auctionDTO);

    }

    @Override
    public void removeAuction() {

        File file = new File("addAuction.ser");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Serialized file deleted successfully");
            } else {
                System.err.println("Failed to delete the serialized file");
            }
        } else {
            System.err.println("Serialized file not found");
        }
    }

    @Override
    public AuctionDTO  notifytoUser() {
        return auctionManage.deserializeAuctionInventory("addAuction.ser");
    }
}
