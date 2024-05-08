package com.example.BidZone.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.io.Serializable;

@Getter
@Setter
@Controller
public class UserMailAndOTPSerailzeble  implements Serializable {
    private String OTP;
    private String email;

}
