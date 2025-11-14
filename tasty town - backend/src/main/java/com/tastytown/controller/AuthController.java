package com.tastytown.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tastytown.constants.Role;
import com.tastytown.dto.AuthRequest;
import com.tastytown.dto.AuthResponse;
import com.tastytown.entity.UserEntity;
import com.tastytown.repository.UserRepository;
import com.tastytown.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserEntity user = userRepository.findByUserEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found with email " + request.getEmail()));
        String jwt = jwtUtil.generateToken(user.getUserId(), user.getRole());

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        if (userRepository.findByUserEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered.");
        }

        UserEntity user = new UserEntity();
        user.setUserEmail(request.getEmail());
        user.setUserPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    // // for admin only
    // @PreAuthorize("hasRole('ADMIN')")
    // @PostMapping("/register-admin")
    // public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest request) {
    //     if (userRepository.findByEmail(request.getEmail()).isPresent()) {
    //         return ResponseEntity.badRequest().body("Email already registered.");
    //     }

    //     UserEntity user = new UserEntity();
    //     user.setEmail(request.getEmail());
    //     user.setPassword(passwordEncoder.encode(request.getPassword()));
    //     user.setRole(Role.ROLE_ADMIN);

    //     userRepository.save(user);
    //     return ResponseEntity.ok("Admin registered successfully.");
    // }

}
