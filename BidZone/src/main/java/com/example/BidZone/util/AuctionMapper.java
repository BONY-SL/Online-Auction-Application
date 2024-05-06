package com.example.BidZone.util;


import com.example.BidZone.dto.AuctionDTO;
import com.example.BidZone.dto.BidDTO;
import com.example.BidZone.dto.ItemDTO;
import com.example.BidZone.dto.UserDTO;
import com.example.BidZone.entity.Auction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuctionMapper {


    @Autowired
    private ModelMapper modelMapper;

    public AuctionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AuctionDTO convertToDto(final Auction auction) {

        AuctionDTO auctionDTO = modelMapper.map(auction, AuctionDTO.class);

        if (auction.getName() != null) {
            ItemDTO itemDTO = modelMapper.map(auction.getName(), ItemDTO.class);
            itemDTO.setAuctionId(auctionDTO.getId());
            auctionDTO.setItem(itemDTO);
        }
        if (auction.getCreatedBy() != null) {
            UserDTO userDTO = modelMapper.map(auction.getCreatedBy(), UserDTO.class);
            auctionDTO.setCreatedById(userDTO.getId());
        }
        if (auction.getCurrentHighestBid() != null) {

            BidDTO bidDTO = modelMapper.map(auction.getCurrentHighestBid(), BidDTO.class);
            auctionDTO.setCurrentHighestBid(bidDTO);

            if (auction.getCurrentHighestBid().getPlacedBy() != null) {
                bidDTO.setPlacedByUsername(auction.getCurrentHighestBid().getPlacedBy().getUsername());
                bidDTO.setPlacedById(auction.getCurrentHighestBid().getPlacedBy().getId());
            }
        }
        return auctionDTO;
    }
}
