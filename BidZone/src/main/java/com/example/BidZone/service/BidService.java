package com.example.BidZone.service;

import com.example.BidZone.dto.BidDTO;
import com.example.BidZone.entity.Auction;
import com.example.BidZone.entity.Bid;
import com.example.BidZone.entity.BiddingItem;
import com.example.BidZone.entity.User;
import com.example.BidZone.repostry.AuctionRepository;
import com.example.BidZone.repostry.BidRepository;
import com.example.BidZone.repostry.UserRepository;
import com.example.BidZone.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BidService {


    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void bidForAuctionItem(final long auctionId, final String userName, final double amount, final String comment)
            throws CommonAppExceptions {

        final Auction auction = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow();


        if (auction.getIsClosed()) {
            throw new CommonAppExceptions("Invaid user Name", HttpStatus.NOT_FOUND);
        }

        final User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found: " + userName));

        final BiddingItem biddingItem = auction.getName();
        double startingPrice = biddingItem.getStartingPrice();

        if (amount < startingPrice) {
            throw new CommonAppExceptions("Bid Price is Lower Than Starting Price", HttpStatus.NOT_FOUND);
        }

        final Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setPlacedBy(user);
        bid.setAmount(amount);
        bid.setComment(comment);

        if (auction.getCreatedBy().getId().equals(user.getId())) {
            throw new CommonAppExceptions("You Cannot Did Your Own Listings", HttpStatus.NOT_FOUND);
        }
        Bid highestBid = auction.getCurrentHighestBid();
        if (highestBid != null && highestBid.getAmount() >= amount) {
            throw new CommonAppExceptions("Bid Amount is les than Current Highest Bid Amout", HttpStatus.NOT_FOUND);

        }

        auction.setCurrentHighestBid(bid);
        bidRepository.save(bid);
    }

    public List<BidDTO> getTheAllBidsUnderTheAuction(final long auctionId) {
        List<Bid> bids = bidRepository.findAllByAuctionIdOrderByAmountDesc(auctionId);
        return bids.stream().map(this::convertToDto).toList();
    }
    private BidDTO convertToDto(final Bid bid) {

        BidDTO bidDTO = new BidDTO();
        bidDTO.setId(bid.getId());
        bidDTO.setPlacedAt(bid.getPlacedAt());
        bidDTO.setAmount(bid.getAmount());
        bidDTO.setPlacedByUsername(bid.getPlacedBy() != null ? bid.getPlacedBy().getUsername() : null);
        bidDTO.setPlacedById(bid.getPlacedBy() != null ? bid.getPlacedBy().getId() : null);
        bidDTO.setComment(bid.getComment());
        return bidDTO;

    }

}
