package com.example.BidZone.controller;
import com.example.BidZone.dto.AuctionDTO;
import com.example.BidZone.dto.UserDTO;
import com.example.BidZone.entity.User;
import com.example.BidZone.repostry.AuctionRepository;
import com.example.BidZone.repostry.UserRepository;
import com.example.BidZone.service.AuctionFactory;
import com.example.BidZone.service.AuctionService;
import com.example.BidZone.service.UserService;
import com.example.BidZone.util.AppExceptions;
import com.example.BidZone.util.AuctionNotFoundException;
import com.example.BidZone.util.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/getAuctiondetails")
    public AuctionDTO getAuctiondetails(@RequestParam Long id) throws AuctionNotFoundException {
        return auctionService.getAuctiondetails(id);
    }

    @PostMapping(value="/createNewAuctions",consumes = {"multipart/form-data"})
    public AuctionDTO  createNewAuctions(@RequestPart("auction") AuctionDTO auctionDTO,
                                         @RequestPart(required = false) MultipartFile image,
                                         @RequestParam(value = "username", required = false) String username){
        System.out.println(auctionDTO);
        System.out.println(image);
        System.out.println(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppExceptions("Invaid user Name", HttpStatus.NOT_FOUND));


        try{
            return auctionService.createNewAuctions(auctionDTO,user,image);

        }catch (CategoryNotFoundException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Category");
        }catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getAllauctions")
    public List<AuctionDTO> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @GetMapping("/getUserDetails")
    public UserDTO getUserDetails(@RequestParam(value = "id", required = false) Long id) {
        System.out.println(id);
        return userService.getUserDetailsById(id);
    }


    @Autowired
    private AuctionFactory auctionFactory;

    @GetMapping("/getAuctionsByCategory")
    public List<AuctionDTO> getAuctionsByCategory(@RequestParam String category) {
        return auctionFactory.getAuctionsByCategory(category);
    }


}
