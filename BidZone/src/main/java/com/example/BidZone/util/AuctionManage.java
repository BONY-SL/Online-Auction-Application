package com.example.BidZone.util;

import com.example.BidZone.dto.AuctionDTO;
import org.springframework.stereotype.Service;

import java.io.*;


@Service
public class AuctionManage {


    synchronized public void serializeAuctionInventory(AuctionDTO auctionDTO) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("addAuction.ser"))) {
            oos.writeObject(auctionDTO);
            System.out.println("User AuctionDTO serialized successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred during serialization: " + e.getMessage());
        }
    }

    synchronized public AuctionDTO deserializeAuctionInventory(String filename) {

        AuctionDTO DeauctionDTO = new AuctionDTO();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {

            DeauctionDTO = (AuctionDTO) ois.readObject();


            System.out.println("User AuctionDTO deserialized successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred during deserialization: " + e.getMessage());
        }
        return DeauctionDTO;
    }
}
