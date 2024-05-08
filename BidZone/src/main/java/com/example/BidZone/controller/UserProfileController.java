package com.example.BidZone.controller;


import com.example.BidZone.dto.UserProfileUpdateDTO;
import com.example.BidZone.util.CommonAppExceptions;
import com.example.BidZone.util.ProfileNotFoundException;
import com.example.BidZone.dto.UserProfileDTO;
import com.example.BidZone.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/auctionappBidZone")
@Controller
public class UserProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile(@RequestParam(value = "username", required = false) String username, Principal principal) throws CommonAppExceptions {
        String effectiveUsername;
        System.out.println(3);
        if (username != null && !username.isEmpty()) {
            effectiveUsername = username;
        } else if (principal != null) {
            effectiveUsername = principal.getName();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        UserProfileDTO profile = profileService.getProfile(effectiveUsername);
        return ResponseEntity.ok(profile);
    }



    @GetMapping("/profile/{profileId}")
    public UserProfileDTO getProfile(@PathVariable Long profileId) throws ProfileNotFoundException {
        System.out.println("Profile ID: " + profileId);
        return profileService.getProfile(profileId);
    }


    @PatchMapping(value = "/profile", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateProfile(
            @RequestPart("profile") final UserProfileUpdateDTO profileUpdateRequest,
            @RequestPart(required = false) final MultipartFile image,
            @RequestParam("username") String username) {

        System.out.println(profileUpdateRequest);
        System.out.println(image);
        System.out.println(username);

        try {
            UserProfileDTO updatedProfile = profileService.updateProfile(profileUpdateRequest, image, username);
            return ResponseEntity.ok(updatedProfile);
        } catch (CommonAppExceptions e) {
            HttpStatus status = e.getHttpStatus();
            return ResponseEntity.status(status).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file");
        }
    }



}
