package com.example.demo.services;

import com.example.demo.dtos.UserDTO;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void saveUser(UserDTO userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        if(userRepository.findByUserName(user.getUserName()) != null){
            log.warn("Processing: User already exists");
            throw new IllegalArgumentException("User already exists");
        }
        userRepository.save(user);
        log.info("Processing: User: "+user+" added successfully!");
    }
}
