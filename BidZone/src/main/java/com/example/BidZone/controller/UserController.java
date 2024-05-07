package com.example.BidZone.controller;
import com.example.BidZone.dto.CreateUserDTO;
import com.example.BidZone.dto.LoginUserDTO;
import com.example.BidZone.dto.UserDTO;
import com.example.BidZone.service.UserService;
import com.example.BidZone.util.CommonAppExceptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        try {
            userService.registerUser(createUserDTO);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (CommonAppExceptions ex) {
            return  handleCommonAppExceptions (ex);
        }

    }

    @ExceptionHandler(CommonAppExceptions.class)
    public ResponseEntity<BidController.ErrorResponse> handleCommonAppExceptions(CommonAppExceptions ex) {
        BidController.ErrorResponse errorResponse = new BidController.ErrorResponse(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    @PostMapping("/userlogin")
    public ResponseEntity<?> login(@RequestBody @Valid LoginUserDTO loginUserDTO) {
        try {
            UserDTO userDto = userService.login(loginUserDTO);
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (CommonAppExceptions e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/validateUserEmailForResetPassword")
     public ResponseEntity<String> validateUserEmailForResetPassword(@RequestParam(value = "email", required = false) String email) {
        System.out.println(email);
        try {
            boolean userExists = userService.checkUserEmailExists(email);
            if (userExists) {
                return ResponseEntity.ok("true");
            } else {
                return ResponseEntity.ok("false");
            }
        } catch (Exception e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
        }
    }


}
