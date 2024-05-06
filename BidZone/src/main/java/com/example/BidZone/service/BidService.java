package com.example.BidZone.service;

import com.example.BidZone.entity.Auction;
import com.example.BidZone.entity.Bid;
import com.example.BidZone.entity.User;
import com.example.BidZone.repostry.AuctionRepository;
import com.example.BidZone.repostry.BidRepository;
import com.example.BidZone.repostry.UserRepository;
import com.example.BidZone.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class BidService {


    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE)

    public void bidForAuctionItem(final long auctionId, final String userName, final double amount, final String comment)
            throws AuctionNotFoundException, BidAmountLessException, BidForSelfAuctionException, AuctionIsClosedException {

        final Auction auction = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow(AuctionNotFoundException::new);

        final User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found: " + userName));

        if (auction.getIsClosed()) {
            throw new AuctionIsClosedException();
        }
        if (auction.getCreatedBy().getId().equals(user.getId())) {
            throw new BidForSelfAuctionException();
        }
        Bid highestBid = auction.getCurrentHighestBid();
        if (highestBid != null && highestBid.getAmount() >= amount) {
            throw new BidAmountLessException();
        }
        final Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setPlacedBy(user);
        bid.setAmount(amount);
        bid.setComment(comment);

        auction.setCurrentHighestBid(bid);
        bidRepository.save(bid);
    }
}
