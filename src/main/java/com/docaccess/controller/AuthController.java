package com.docaccess.controller;

import com.docaccess.dto.ApiResponse;
import com.docaccess.dto.LoginResponse;
import com.docaccess.dto.LoginRequest;
import com.docaccess.dto.RegisterRequest;
import com.docaccess.security.JwtUtil;
import com.docaccess.service.UserService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j

public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtProvider;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(new ApiResponse<>("User registered successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid@RequestBody  LoginRequest request) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String token = jwtProvider.generateToken(request.getUsername());
            log.info("User '{}' logged in successfully.", authentication.getName());
            authentication.getAuthorities().forEach(auth ->
                    log.debug("Authority: {}", auth.getAuthority()));

        return ResponseEntity.ok(new ApiResponse<>("Login successful", new LoginResponse(token)));
    }

}