package com.example.demo.controllers;

import com.example.demo.dtos.UserDTO;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/cv")
@CrossOrigin(origins = "${fe.base-url}",
        allowedHeaders = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH}
)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/save-user")
    public ResponseEntity<String> saveUser(@RequestBody UserDTO userDTO) {
        log.info("Request data: " + userDTO);
        try {
            userService.saveUser(userDTO);
            log.info("Response data: User created");
            return ResponseEntity.status(HttpStatus.CREATED).body("User created");
        } catch (Exception e) {
            log.error("Response data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body("User already exists");
        }

    }
}

