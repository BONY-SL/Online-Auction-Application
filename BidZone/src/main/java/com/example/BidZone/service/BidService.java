package com.example.BidZone.service;

import com.example.BidZone.dto.BidDTO;
import com.example.BidZone.dto.MyBidDTO;
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

import java.util.List;
import java.util.stream.Collectors;


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

    //this method use for Under the AuctionId get All Bids are placed
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

    public List<MyBidDTO> findBidsPlacedByUser(final String userName) {
        List<Bid> bids = bidRepository.findBidByPlacedByUsernameOrderByPlacedAtDesc(userName);

        return bids.stream()
                .map(bid -> {
                    MyBidDTO myBidDTO = modelMapper.map(bid, MyBidDTO.class);
                    // Ensure the auctionId is set if the auction object is not null
                    if (bid.getAuction() != null) {
                        myBidDTO.setAuctionId(bid.getAuction().getId());
                        myBidDTO.setAuctionName(bid.getAuction().getAction_name());
                        myBidDTO.setAuctionClosingTime(bid.getAuction().getClosingTime());

                        if (bid.getAuction().getCurrentHighestBid() != null) {
                            myBidDTO.setAuctionCurrentHighestBidAmount(bid.getAuction().getCurrentHighestBid().getAmount());
                        }
                    }
                    myBidDTO.setAuctionCurrentHighestBidAmount(bid.getAmount());
                    return myBidDTO;
                })
                .collect(Collectors.toList());
    }






}
