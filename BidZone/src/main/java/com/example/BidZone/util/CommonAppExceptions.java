package com.example.BidZone.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonAppExceptions extends RuntimeException{

    private final HttpStatus status;

    public CommonAppExceptions(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public HttpStatus getHttpStatus() {
        return status;
    }

}
