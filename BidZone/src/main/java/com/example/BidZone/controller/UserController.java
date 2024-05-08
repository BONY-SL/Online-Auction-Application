package com.example.BidZone.controller;
import com.example.BidZone.dto.CreateUserDTO;
import com.example.BidZone.dto.LoginUserDTO;
import com.example.BidZone.dto.MailRequestDTO;
import com.example.BidZone.dto.UserDTO;
import com.example.BidZone.service.EmailService;
import com.example.BidZone.service.UserService;
import com.example.BidZone.util.CommonAppExceptions;
import com.example.BidZone.util.OTPMange;
import com.example.BidZone.util.UserMailAndOTPSerailzeble;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPMange otpMange;


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

    @PostMapping("/sendmailToUser")
    synchronized public ResponseEntity<?> sendMailToUser(@RequestBody MailRequestDTO mailRequest) {

        try {
            String otpCode = generateOTP();

            //Seralization in User OTP and Email
            UserMailAndOTPSerailzeble userMailAndOTPSerailzeble=new UserMailAndOTPSerailzeble();
            userMailAndOTPSerailzeble.setOTP(otpCode);
            userMailAndOTPSerailzeble.setEmail(mailRequest.getTo());
            otpMange.serializeInventory(otpCode,userMailAndOTPSerailzeble);

            String emailContent = mailRequest.getContent() + "\n\nYour OTP code: " + otpCode;

            emailService.sendUserOTPcODE(mailRequest.getTo(), mailRequest.getSubject(), emailContent);
            return new ResponseEntity<>("Email sent successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    synchronized private String generateOTP() {

        synchronized (this){
            Random random = new Random();
            int otp = 10000 + random.nextInt(90000000);
            return String.valueOf(otp);
        }

    }

    @PostMapping("/verifyGetOtp")
    public ResponseEntity<?> verifyGetOtp(@RequestParam(value = "otp", required = false) String otp){
        try {
            UserMailAndOTPSerailzeble userMailAndOTPDeSerailzeble=otpMange.deserializeInventory(otp);

            if (!userMailAndOTPDeSerailzeble.getOTP().isEmpty() && !userMailAndOTPDeSerailzeble.getEmail().isEmpty() && userMailAndOTPDeSerailzeble.getOTP().equals(otp)) {
                return ResponseEntity.ok(userMailAndOTPDeSerailzeble.getOTP());
            } else {
                return ResponseEntity.ok("false");
            }
        } catch (Exception e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid OTP Code");
        }
    }

    @PutMapping ("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam(value = "otp", required = false) String otp,
                                           @RequestParam(value = "password", required = false) String password){

        try {
            UserMailAndOTPSerailzeble userMailAndOTPDeSerailzeble=otpMange.deserializeInventory(otp);

            userService.resetPassword(userMailAndOTPDeSerailzeble,password);

            return ResponseEntity.ok(userMailAndOTPDeSerailzeble);
        } catch (Exception e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid User Details");
        }

    }

}
