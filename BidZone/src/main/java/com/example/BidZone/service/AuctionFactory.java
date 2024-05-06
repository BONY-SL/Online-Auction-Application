package com.example.BidZone.service;


import com.example.BidZone.dto.AuctionDTO;
import com.example.BidZone.repostry.AuctionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionFactory {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuctionRepository auctionRepository;

    public List<AuctionDTO> getAuctionsByCategory(Long category) {
        return auctionRepository.findAll().stream()
                .filter(auction -> auction.getName().getCategory().getId().equals(category))
                .map(auction -> modelMapper.map(auction, AuctionDTO.class))
                .collect(Collectors.toList());
    }
}
