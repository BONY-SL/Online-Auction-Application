package com.example.BidZone.repostry;


import com.example.BidZone.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentDetails,Long> {


    @Query(value = "SELECT * FROM payment_details WHERE username = ?1", nativeQuery = true)
    List<PaymentDetails> getPaymentDetailsByName(String username);
}
