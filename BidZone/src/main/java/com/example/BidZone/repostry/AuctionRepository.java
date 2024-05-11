package com.example.BidZone.repostry;


import com.example.BidZone.entity.Auction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Long> {
    @Query("SELECT a FROM Auction a WHERE a.id = :auctionId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Auction> findByIdWithLock(@Param("auctionId") Long auctionId);

    Auction findAuctionsById(Long id);


    List<Auction> findAuctionByCreatedByUsername(final String userName);

    List<Auction> findAuctionByCreatedByUsernameOrderByCreatedAtDesc(final String userName);

    List<Auction> findAuctionByCreatedByUsernameOrderByCreatedAtAsc(final String userName);

    List<Auction> findAuctionsByCreatedByUsernameOrderByClosingTimeDesc(final String userName);

    List<Auction> findAuctionsByCreatedByUsernameOrderByClosingTimeAsc(final String userName);




}
