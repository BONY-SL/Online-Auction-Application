package com.example.BidZone.service;


import com.example.BidZone.dto.PaymentDTO;
import com.example.BidZone.entity.Auction;
import com.example.BidZone.entity.PaymentDetails;
import com.example.BidZone.repostry.AuctionRepository;
import com.example.BidZone.repostry.PaymentRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AuctionRepository auctionRepository; // Assuming you have an AuctionRepository

    @Autowired
    private ModelMapper modelMapper;

    public PaymentDTO savePaymentDetails(PaymentDTO paymentDTO){
        // Fetch the Auction entity based on auctionid
        Auction auction = auctionRepository.findById(Long.parseLong(paymentDTO.getAuctionid())).orElse(null);

        if (auction == null) {
            // Handle the case where the auction is not found
            // You can throw an exception or handle it based on your application logic
            throw new IllegalArgumentException("Auction not found for ID: " + paymentDTO.getAuctionid());
        }

        // Map PaymentDTO to PaymentDetails
        PaymentDetails paymentDetails = modelMapper.map(paymentDTO, PaymentDetails.class);
        paymentDetails.setAuction(auction); // Set the auction in PaymentDetails
        paymentRepository.save(paymentDetails);

        return paymentDTO;
    }

    public List<PaymentDTO> getAllPaymentDetails(String username) {
        List<PaymentDetails> paymentDetailsList = paymentRepository.getPaymentDetailsByName(username);
        return modelMapper.map(paymentDetailsList, new TypeToken<List<PaymentDTO>>() {}.getType());
    }
}

