package com.example.BidZone.service;


import com.example.BidZone.AppExceptions;
import com.example.BidZone.dto.CreateUserDTO;
import com.example.BidZone.entity.User;
import com.example.BidZone.entity.UserProfile;
import com.example.BidZone.repostry.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

}
