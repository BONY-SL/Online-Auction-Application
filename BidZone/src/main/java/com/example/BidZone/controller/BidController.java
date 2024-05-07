package com.example.BidZone.controller;


import com.example.BidZone.dto.BidDTO;
import com.example.BidZone.dto.BidRequestDTO;
import com.example.BidZone.dto.BidToItemDTO;
import com.example.BidZone.dto.MyBidDTO;
import com.example.BidZone.service.BidService;
import com.example.BidZone.util.AuctionIsClosedException;
import com.example.BidZone.util.AuctionNotFoundException;
import com.example.BidZone.util.BidAmountLessException;
import com.example.BidZone.util.BidForSelfAuctionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Collections;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
public class BidController {

    @Autowired
    private BidService bidService;


    @PostMapping("/bidForAuctionItem")
    public ResponseEntity<?> bidForAuctionItem(@RequestBody BidRequestDTO bidRequestDTO) {
        Long auctionId = bidRequestDTO.getAuctionId();
        String username = bidRequestDTO.getUsername();
        BidToItemDTO bidToItemDTO = bidRequestDTO.getBidToItemDTO();

        System.out.println(bidToItemDTO);
        System.out.println(auctionId);
        System.out.println(username);

        try {
            bidService.bidForAuctionItem(auctionId, username, bidToItemDTO.getAmount(), bidToItemDTO.getComment());
            return ResponseEntity.ok("Bid placed successfully");
        } catch (AuctionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Auction does not exist"));
        } catch (BidAmountLessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Bid amount smaller than highest bid"));
        } catch (BidForSelfAuctionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Cannot place bid on own auction"));
        } catch (AuctionIsClosedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Auction is closed"));
        }
    }

    @GetMapping("/getTheAllBidsUnderTheAuction")
    public List<BidDTO> getTheAllBidsUnderTheAuction(@RequestParam long auctionId){
        return bidService.getTheAllBidsUnderTheAuction(auctionId);
    }

    @GetMapping("/myBids")
    public List<MyBidDTO> findBidsPlacedByUser(@RequestParam(value = "username", required = false) String username) {
        System.out.println(username);

        return bidService.findBidsPlacedByUser(username);
    }



}

