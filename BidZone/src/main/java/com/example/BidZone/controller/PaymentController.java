


package com.example.BidZone.controller;


import com.example.BidZone.dto.PaymentDTO;
import com.example.BidZone.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/savepayment")
    public PaymentDTO savePaymentDetails(@RequestBody PaymentDTO paymentDTO){
        System.out.println("work");
        return paymentService.savePaymentDetails(paymentDTO);
    }

    @GetMapping("/getpaymentdetails/{username}")
    public List<PaymentDTO> getPaymentDetails(@PathVariable String username) {
        return paymentService.getAllPaymentDetails(username);
    }
}
