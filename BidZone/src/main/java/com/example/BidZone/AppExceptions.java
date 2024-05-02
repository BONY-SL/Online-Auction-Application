package com.example.BidZone;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppExceptions extends RuntimeException{

    private final HttpStatus status;

    public AppExceptions(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
