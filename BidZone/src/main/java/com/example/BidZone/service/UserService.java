package com.example.BidZone.service;
import com.example.BidZone.dto.*;
import com.example.BidZone.util.CommonAppExceptions;
import com.example.BidZone.entity.User;
import com.example.BidZone.entity.UserProfile;
import com.example.BidZone.repostry.UserRepository;
import com.example.BidZone.util.UserMailAndOTPSerailzeble;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void registerUser(final CreateUserDTO userDTO) throws CommonAppExceptions {
        if(userRepository.existsByUsername(userDTO.getUserName())){
            throw new CommonAppExceptions("Username already exists", HttpStatus.BAD_REQUEST);
        }if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new CommonAppExceptions("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User user=new User();
        user.setUsername(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        UserProfile userProfile=new UserProfile();
        userProfile.setFirstName(userDTO.getFirstName());
        userProfile.setLastName(userDTO.getLastName());
        user.setUserProfile(userProfile);
        userRepository.save(user);

    }

    public void resetPassword(UserMailAndOTPSerailzeble userMailAndOTPSerailzeble,String password) throws CommonAppExceptions {

        User user=userRepository.findByEmail(userMailAndOTPSerailzeble.getEmail())
                .orElseThrow(()->new CommonAppExceptions("Invalid Email Address",HttpStatus.NOT_FOUND));

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);


    }

    public UserDTO login(LoginUserDTO loginUserDTO) throws CommonAppExceptions {
        User user = userRepository.findByUsername(loginUserDTO.getUsername())
                .orElseThrow(() -> new CommonAppExceptions("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(loginUserDTO.getPassword()), user.getPassword())) {
            UserDTO userDto = modelMapper.map(user, UserDTO.class);
            UserProfileDTO userProfileDTO = modelMapper.map(user.getUserProfile(), UserProfileDTO.class);
            userDto.setProfile(userProfileDTO);
            System.out.println(userDto);
            return userDto;
        }
        throw new CommonAppExceptions("Invalid password", HttpStatus.BAD_REQUEST);
    }
    public UserDTO getUserDetailsById(Long id) {
        System.out.println(id);
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            UserProfileDTO profileDTO = new UserProfileDTO();
            profileDTO.setId(user.getUserProfile().getId());
            profileDTO.setFirstName(user.getUserProfile().getFirstName());
            profileDTO.setLastName(user.getUserProfile().getLastName());
            profileDTO.setDescription(user.getUserProfile().getDescription());
            profileDTO.setProfilePictureURL(user.getUserProfile().getProfilePictureS3URL());
            userDTO.setProfile(profileDTO);
            return userDTO;
        } else {
            throw new RuntimeException("User not found");
        }
    }

     public boolean checkUserEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}


