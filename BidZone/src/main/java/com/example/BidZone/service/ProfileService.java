package com.example.BidZone.service;
import com.example.BidZone.dto.UserProfileUpdateDTO;
import com.example.BidZone.util.AppExceptions;
import com.example.BidZone.util.ProfileNotFoundException;
import com.example.BidZone.dto.UserProfileDTO;
import com.example.BidZone.entity.User;
import com.example.BidZone.entity.UserProfile;
import com.example.BidZone.repostry.ProfileRepository;
import com.example.BidZone.repostry.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserProfileDTO getProfile(final String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new AppExceptions("Unknown user", HttpStatus.NOT_FOUND));
        return convertToDto(user.getUserProfile());
    }

    public UserProfileDTO getProfile(long profileId) throws ProfileNotFoundException {
        final UserProfile profile = profileRepository.findById(profileId).orElseThrow(ProfileNotFoundException::new);
        return convertToDto(profile);
    }

    private UserProfileDTO convertToDto(final UserProfile profile) {
        return modelMapper.map(profile, UserProfileDTO.class);
    }

    public UserProfileDTO updateProfile(final UserProfileUpdateDTO profileUpdateRequest, final MultipartFile image, final String userName) throws IOException {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new AppExceptions("Unknown user", HttpStatus.NOT_FOUND));
        final UserProfile existingProfile = user.getUserProfile();
        modelMapper.map(profileUpdateRequest, existingProfile);

        // Save profile picture
        if (image != null && !image.isEmpty()) {
            final String imageFilePath = saveFileToDisk(image, userName);
            existingProfile.setProfilePictureS3URL(imageFilePath);
        }
        final UserProfile savedProfile = profileRepository.save(existingProfile);

        UserProfileDTO dto = convertToDto(savedProfile);
        dto.setProfilePictureURL(savedProfile.getProfilePictureS3URL());
        return dto;
    }


    /*in this method create for the upload to the image to database the database saved image path url the image actualy
    saved in the local disk connect the database link path
    */
    public String saveFileToDisk(final MultipartFile image, String userName) throws IOException {
        String targetDir = "C:\\Users\\Public\\uploads\\images\\" + userName;
        File directory = new File(targetDir);

        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + targetDir);
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        File targetFile = new File(directory, fileName);

        try {
            image.transferTo(targetFile);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new IOException("Failed to save file to disk", e);
        }

        return "/uploads/images/" + userName + "/" + fileName;
    }

}
