package com.example.BidZone.controller;
import com.example.BidZone.dto.CreateUserDTO;
import com.example.BidZone.dto.LoginUserDTO;
import com.example.BidZone.dto.UserDTO;
import com.example.BidZone.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        try {
            userService.registerUser(createUserDTO);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/userlogin")
    public ResponseEntity<?> login(@RequestBody @Valid LoginUserDTO loginUserDTO) {
        try {
            UserDTO userDto = userService.login(loginUserDTO);
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
