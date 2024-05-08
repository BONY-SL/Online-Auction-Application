package com.example.BidZone.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CommonAppExceptions extends Exception{

    private final HttpStatus status;

    public CommonAppExceptions(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public HttpStatus getHttpStatus() {
        return status;
    }

}
