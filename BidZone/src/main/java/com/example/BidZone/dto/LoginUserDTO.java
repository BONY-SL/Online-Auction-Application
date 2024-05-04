package com.example.BidZone.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class LoginUserDTO {

    @NotNull
    private String username;
    @NotNull
    private String password;
}
