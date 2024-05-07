package com.example.BidZone.service;
import com.example.BidZone.dto.AuctionDTO;
import com.example.BidZone.entity.Auction;
import com.example.BidZone.repostry.AuctionRepository;
import com.example.BidZone.util.AuctionMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionFactory {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private AuctionMapper auctionMapper;

    public List<AuctionDTO> getAuctionsByCategory(Long category) {
        return auctionRepository.findAll().stream()
                .filter(auction -> auction.getName().getCategory().getId().equals(category))
                .map(auctionMapper::convertToDto).collect(Collectors.toList());
    }

    public List<AuctionDTO> getMyAllLisingSpesificOrder(String userName, String order) {

        List<Auction> auctionsList;

        if ("all".equals(order)) {
            auctionsList = auctionRepository.findAuctionByCreatedByUsername(userName);
        } else if ("latest_first".equals(order)) {
            auctionsList = auctionRepository.findAuctionByCreatedByUsernameOrderByCreatedAtDesc(userName);
        } else if ("oldest_first".equals(order)) {
            auctionsList = auctionRepository.findAuctionByCreatedByUsernameOrderByCreatedAtAsc(userName);
        } else if ("closing_later".equals(order)) {
            auctionsList = auctionRepository.findAuctionsByCreatedByUsernameOrderByClosingTimeDesc(userName);
        } else if ("closing_earlier".equals(order)) {
            auctionsList = auctionRepository.findAuctionsByCreatedByUsernameOrderByClosingTimeAsc(userName);
        } else {
            auctionsList = new ArrayList<>();
        }

        return auctionsList.stream().map(auctionMapper::convertToDto).collect(Collectors.toList());
    }


}
