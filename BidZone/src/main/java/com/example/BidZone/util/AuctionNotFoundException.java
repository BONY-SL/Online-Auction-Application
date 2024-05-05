package com.example.BidZone.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Auction not found")
public class AuctionNotFoundException extends Exception {
}
