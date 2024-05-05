package com.example.BidZone.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Auction has closed")
public class AuctionIsClosedException extends Exception {
}

