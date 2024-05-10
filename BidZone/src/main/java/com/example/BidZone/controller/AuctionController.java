package com.example.BidZone.controller;
import com.example.BidZone.dto.AuctionDTO;
import com.example.BidZone.dto.UserDTO;
import com.example.BidZone.entity.User;
import com.example.BidZone.repostry.AuctionRepository;
import com.example.BidZone.repostry.UserRepository;
import com.example.BidZone.util.AuctionFactory;
import com.example.BidZone.service.AuctionService;
import com.example.BidZone.service.UserService;
import com.example.BidZone.util.AuctionNotifyInfo;
import com.example.BidZone.util.CommonAppExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
@Controller
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuctionFactory auctionFactory;

    @Autowired
    private AuctionNotifyInfo auctionNotifyInfo;

    @GetMapping("/getAuctiondetails")
    public AuctionDTO getAuctiondetails(@RequestParam Long id) throws CommonAppExceptions {
        return auctionService.getAuctiondetails(id);

    }

    @PostMapping(value="/createNewAuctions",consumes = {"multipart/form-data"})
    public AuctionDTO  createNewAuctions(@RequestPart("auction") AuctionDTO auctionDTO,
                                         @RequestPart(required = false) MultipartFile image,
                                         @RequestParam(value = "username", required = false) String username) throws CommonAppExceptions {
        System.out.println(auctionDTO);
        System.out.println(image);
        System.out.println(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CommonAppExceptions("Invaid user Name", HttpStatus.NOT_FOUND));

        try{
            return auctionService.createNewAuctions(auctionDTO,user,image);
        }catch (CommonAppExceptions e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Category");
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    @GetMapping("/getAuctionsByCategory")
    public List<AuctionDTO> getAuctionsByCategory(@RequestParam Long category) {
        return auctionFactory.getAuctionsByCategory(category);
    }

    @GetMapping("/getmyAllAuctions")
    List<AuctionDTO> getmyAllAuctions(@RequestParam(value = "username", required = false) String username) {

        return auctionService.getmyAllAuctions(username);
    }

    @GetMapping("/getMyAllLisingSpesificOrder")
    List<AuctionDTO> getMyAllLisingSpesificOrder(@RequestParam(value = "username", required = false) String username,
                                                 @RequestParam(value = "order", required = false) String order){
        return auctionFactory.getMyAllLisingSpesificOrder(username,order);
    }

    @PutMapping("/updateAuction")
    public AuctionDTO updateAuction(@RequestBody AuctionDTO auctionDTO) throws CommonAppExceptions {
        return auctionService.updateAuction(auctionDTO);
    }

    @GetMapping("/getNotifyMessageAddNewAuction")
    public ResponseEntity<Map<String, String>> getNotifyMessageAddNewAuction(){
        Map<String, String> response = new HashMap<>();
        response.put("message", auctionNotifyInfo.andNewAuctionNotification());
        return ResponseEntity.ok(response);
    }

}
