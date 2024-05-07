package com.example.BidZone.controller;


import com.example.BidZone.dto.BidDTO;
import com.example.BidZone.dto.BidRequestDTO;
import com.example.BidZone.dto.BidToItemDTO;
import com.example.BidZone.dto.ItemDTO;
import com.example.BidZone.service.BidService;
import com.example.BidZone.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
@Controller
public class BidController {

    @Autowired
    private BidService bidService;


    @PostMapping("/bidForAuctionItem")
    public ResponseEntity<?> bidForAuctionItem(@RequestBody BidRequestDTO bidRequestDTO) {
        Long auctionId = bidRequestDTO.getAuctionId();
        String username = bidRequestDTO.getUsername();
        BidToItemDTO bidToItemDTO = bidRequestDTO.getBidToItemDTO();

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
        }catch (AuctionIsClosedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message","Auction Is Closed"));
        }catch (BidBelowStartingPriceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Bid amount is less than starting price"));
        }
    }

    @GetMapping("/getTheAllBidsUnderTheAuction")
    public List<BidDTO> getTheAllBidsUnderTheAuction(@RequestParam long auctionId){
        return bidService.getTheAllBidsUnderTheAuction(auctionId);
    }
}

