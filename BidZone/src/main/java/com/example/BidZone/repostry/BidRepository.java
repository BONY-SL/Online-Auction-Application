package com.example.BidZone.repostry;

import com.example.BidZone.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByAuctionIdOrderByAmountDesc(long auctionId);
    List<Bid> findBidByPlacedByUsernameOrderByPlacedAtDesc(final String userName);
}
