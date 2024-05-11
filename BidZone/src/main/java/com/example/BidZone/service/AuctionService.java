package com.example.BidZone.service;
import com.example.BidZone.dto.AuctionDTO;
import com.example.BidZone.dto.CategoryDTO;
import com.example.BidZone.dto.ItemDTO;
import com.example.BidZone.entity.*;
import com.example.BidZone.repostry.AuctionRepository;
import com.example.BidZone.repostry.CategoryRepository;
import com.example.BidZone.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@Component
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

    @Autowired
    private CreateAuctionFactory createAuctionFactory;



    public AuctionDTO createNewAuctions(AuctionDTO auctionDTO, User user, MultipartFile image) throws CommonAppExceptions, IOException {

        Auction auction = convertToEntity(auctionDTO);

        ItemDTO itemDto = auctionDTO.getItem();
        Optional<Category> category = categoryRepository.findById(itemDto.getCategory().getId());
        if (category.isEmpty()) {
            throw new CommonAppExceptions("Category Is Empty", HttpStatus.NOT_FOUND);
        }

        BiddingItem item = new BiddingItem(itemDto.getName(), category.get());
        item.setAuction(auction);
        item.setDescription(itemDto.getDescription());
        item.setStartingPrice(itemDto.getStartingPrice());
        auction.setName(item);
        auction.setCreatedBy(user);
        item.setCategory(category.get());



        if (image != null && !image.isEmpty()) {
            final String imagePath = saveimageService.saveFileToDisk(image, user.getUsername());
            auction.setImage(imagePath);
            auctionDTO.setImage(imagePath);
        }

        final Auction savedAuction = auctionRepository.save(auction);

        itemDto.setAuctionId(savedAuction.getId());
        auctionDTO.setId(savedAuction.getId());
        auctionDTO.setItem(itemDto);
        itemDto.setId(savedAuction.getId());
        auctionDTO.setCreatedById(user.getId());

        createAuctionFactory.removeAuction();

        createAuctionFactory.addnewAuction(auctionDTO);

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

    public AuctionDTO getAuctiondetails(long auctionId) throws CommonAppExceptions {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        return auctionMapper.convertToDto(auction.orElseThrow(() -> new CommonAppExceptions("Auction Id Not Found", HttpStatus.NOT_FOUND)));
    }

    public List<AuctionDTO> getmyAllAuctions(final String userName) {
        return auctionRepository.findAuctionByCreatedByUsername(userName).stream()
                .map(auctionMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public AuctionDTO updateAuction(AuctionDTO auctionDTO) throws CommonAppExceptions {
        Optional<Auction> optionalAuction = auctionRepository.findById(auctionDTO.getId());

        if (optionalAuction.isEmpty()) {
            throw new CommonAppExceptions("Auction Not Found Exception", HttpStatus.NOT_FOUND);
        }

        System.out.println(auctionDTO);

        Auction auction = optionalAuction.get();
        auction.setAction_name(auctionDTO.getAction_name());
        auction.setDescription(auctionDTO.getDescription());
        auction.setClosingTime(auctionDTO.getClosingTime());

        if (auctionDTO.getItem() != null) {

            auction.getName().setStartingPrice(auctionDTO.getItem().getStartingPrice());

        }
        Auction updatedAuction = auctionRepository.save(auction);
        return auctionMapper.convertToDto(updatedAuction);
    }

    public void deleteAuction(Long auctionId) throws CommonAppExceptions {

        Auction auction=auctionRepository.findById(auctionId)
                .orElseThrow(()->new CommonAppExceptions("Invalid Auction ID",HttpStatus.NOT_FOUND));

        auction.setAction_name("NULL");
        auctionRepository.save(auction);
    }

}

