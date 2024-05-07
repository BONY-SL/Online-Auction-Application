package com.example.BidZone.controller;
import com.example.BidZone.dto.BidDTO;
import com.example.BidZone.dto.BidRequestDTO;
import com.example.BidZone.dto.BidToItemDTO;
import com.example.BidZone.service.BidService;
import com.example.BidZone.util.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
@Controller
public class BidController {

    @Autowired
    private BidService bidService;

    @PostMapping("/bidForAuctionItem")
    public ResponseEntity<?> bidForAuctionItem(@RequestBody BidRequestDTO bidRequestDTO){

        Long auctionId = bidRequestDTO.getAuctionId();
        String username = bidRequestDTO.getUsername();
        BidToItemDTO bidToItemDTO = bidRequestDTO.getBidToItemDTO();
        try {
            bidService.bidForAuctionItem(auctionId, username, bidToItemDTO.getAmount(), bidToItemDTO.getComment());
            return ResponseEntity.ok("Bid placed successfully");
        } catch (CommonAppExceptions ex) {
            return handleCommonAppExceptions(ex);
        }

    }

    @GetMapping("/getTheAllBidsUnderTheAuction")
    public List<BidDTO> getTheAllBidsUnderTheAuction(@RequestParam long auctionId){
        return bidService.getTheAllBidsUnderTheAuction(auctionId);
    }


    @ExceptionHandler(CommonAppExceptions.class)
    public ResponseEntity<ErrorResponse> handleCommonAppExceptions(CommonAppExceptions ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    @Setter
    @Getter
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}

