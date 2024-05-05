package com.example.BidZone.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Category Not Found")
public class CategoryNotFoundException extends Exception {

}
