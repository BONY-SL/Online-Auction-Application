package com.example.BidZone.controller;


import com.example.BidZone.dto.BidToItemDTO;
import com.example.BidZone.service.BidService;
import com.example.BidZone.util.AuctionIsClosedException;
import com.example.BidZone.util.AuctionNotFoundException;
import com.example.BidZone.util.BidAmountLessException;
import com.example.BidZone.util.BidForSelfAuctionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
public class BidController {

    @Autowired
    private BidService bidService;


    @PostMapping("/bidForAuctionItem/{auctionId}/bid")
    public void bidForAuctionItem(@PathVariable long auctionId, final Principal principal,
                                  @RequestBody BidToItemDTO bidToItemDTO)throws AuctionIsClosedException{

        try {
            bidService.bidForAuctionItem(auctionId, principal.getName(), bidToItemDTO.getAmount(), bidToItemDTO.getComment());
        } catch (AuctionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction does not exist");
        } catch (BidAmountLessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bid amount smaller than highest bid");
        } catch (BidForSelfAuctionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot place bid on own auction");
        }

    }
}
