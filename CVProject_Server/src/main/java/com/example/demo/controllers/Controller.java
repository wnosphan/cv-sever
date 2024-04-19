package com.example.demo.controllers;

import com.example.demo.services.KeycloakService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {
    private final KeycloakService keycloakService;

    @PostMapping("/verify-token")
    private ResponseEntity<?> verifyToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {
        log.info("Request data: Token: "+token);
        if (token.isEmpty()){
            log.error("Response data: Token missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token missing");
        }else if (!token.startsWith("Bearer ")){
            log.error("Response data: Format error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Format error");
        }
        String realToken = token.substring(7);
        if (Boolean.TRUE.equals(keycloakService.introspectToken(realToken))) {
            log.info("Response data: True");
            return ResponseEntity.status(HttpStatus.OK).body("True");
        }
        else {
            log.info("Response data: False(Unauthorized)");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("False");
        }
    }

}
