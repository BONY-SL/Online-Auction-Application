package com.example.BidZone.service;


import com.example.BidZone.dto.AuctionDTO;
import com.example.BidZone.dto.BidDTO;
import com.example.BidZone.dto.ItemDTO;
import com.example.BidZone.dto.UserDTO;
import com.example.BidZone.entity.*;
import com.example.BidZone.repostry.AuctionRepository;
import com.example.BidZone.repostry.BidRepository;
import com.example.BidZone.repostry.CategoryRepository;
import com.example.BidZone.util.AuctionMapper;
import com.example.BidZone.util.AuctionNotFoundException;
import com.example.BidZone.util.CategoryNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuctionService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProfileService saveimageService;

    @Autowired
    private AuctionMapper auctionMapper;


    public AuctionDTO createNewAuctions(AuctionDTO auctionDTO, User user, MultipartFile image) throws CategoryNotFoundException, IOException {
        Auction auction = convertToEntity(auctionDTO);

        ItemDTO itemDto = auctionDTO.getItem();
        Optional<Category> category = categoryRepository.findById(itemDto.getCategory().getId());
        if (category.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        BiddingItem item = new BiddingItem(itemDto.getName(), category.get());
        item.setAuction(auction);
        item.setDescription(itemDto.getDescription());
        item.setStartingPrice(itemDto.getStartingPrice());
        auction.setName(item);
        auction.setCreatedBy(user);

        if (image != null && !image.isEmpty()) {
            final String imagePath = saveimageService.saveFileToDisk(image, user.getUsername());
            auction.setImage(imagePath);
        }

        final Auction savedAuction = auctionRepository.save(auction);
        return auctionMapper.convertToDto(savedAuction);
    }

    private Auction convertToEntity(final AuctionDTO auctionDTO) {
        return modelMapper.map(auctionDTO, Auction.class);
    }
    public List<AuctionDTO> getAllAuctions() {

        return auctionRepository.findAll().stream()
                .map(auctionMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public AuctionDTO getAuctiondetails(long auctionId) throws AuctionNotFoundException {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        return auctionMapper.convertToDto(auction.orElseThrow(AuctionNotFoundException::new));
    }

    public List<AuctionDTO> getmyAllAuctions(final String userName) {
        return auctionRepository.findAuctionByCreatedByUsername(userName).stream()
                .map(auctionMapper::convertToDto)
                .collect(Collectors.toList());
    }

}

