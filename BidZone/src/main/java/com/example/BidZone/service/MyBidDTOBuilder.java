package com.example.BidZone.service;

import com.example.BidZone.dto.MyBidDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MyBidDTOBuilder {
    private MyBidDTO myBidDTO;

    public MyBidDTOBuilder() {
        myBidDTO = new MyBidDTO();
    }

    public MyBidDTOBuilder withAuctionId(Long auctionId) {
        myBidDTO.setAuctionId(auctionId);
        return this;
    }

    public MyBidDTOBuilder withAuctionName(String auctionName) {
        myBidDTO.setAuctionName(auctionName);
        return this;
    }

    public MyBidDTOBuilder withCurrentHighestBidAmount(BigDecimal currentHighestBidAmount) {
        myBidDTO.setAuctionCurrentHighestBidAmount(currentHighestBidAmount.doubleValue());
        return this;
    }

    public MyBidDTOBuilder withClosingTime(LocalDateTime closingTime) {
        myBidDTO.setAuctionClosingTime(closingTime);
        return this;
    }

    public MyBidDTOBuilder withAmount(BigDecimal amount) {
        myBidDTO.setAmount(amount.doubleValue());
        return this;
    }

    public MyBidDTO build() {
        return myBidDTO;
    }
}

