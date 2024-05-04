package com.example.BidZone.service;


import com.example.BidZone.util.AppExceptions;
import com.example.BidZone.dto.CreateUserDTO;
import com.example.BidZone.dto.LoginUserDTO;
import com.example.BidZone.dto.UserDTO;
import com.example.BidZone.dto.UserProfileDTO;
import com.example.BidZone.entity.User;
import com.example.BidZone.entity.UserProfile;
import com.example.BidZone.repostry.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(final CreateUserDTO userDTO){
        if(userRepository.existsByUsername(userDTO.getUserName())){
            throw new AppExceptions("Username already exists", HttpStatus.BAD_REQUEST);
        }

        User user=new User();
        user.setUsername(userDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        UserProfile userProfile=new UserProfile();
        userProfile.setFirstName(userDTO.getFirstName());
        userProfile.setLastName(userDTO.getLastName());
        user.setUserProfile(userProfile);
        userRepository.save(user);

    }

    public UserDTO login(LoginUserDTO loginUserDTO) {
        User user = userRepository.findByUsername(loginUserDTO.getUsername())
                .orElseThrow(() -> new AppExceptions("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(loginUserDTO.getPassword()), user.getPassword())) {
            UserDTO userDto = modelMapper.map(user, UserDTO.class);
            UserProfileDTO userProfileDTO = modelMapper.map(user.getUserProfile(), UserProfileDTO.class);
            userDto.setProfile(userProfileDTO);
            return userDto;
        }
        throw new AppExceptions("Invalid password", HttpStatus.BAD_REQUEST);
    }



}
