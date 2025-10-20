package com.example.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
	   private final UserRepository userRepository;
	   private final PasswordEncoder passwordEncoder;
	   
	   @Autowired
	   private JwtUtil jwtutil;
	   
	   public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }
	   
	   @PostMapping("/register")
	    public User register(@RequestBody User user) {
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        return userRepository.save(user);
	    }
	   @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody User loginRequest) {
	        return userRepository.findByUsername(loginRequest.getUsername())
	                .filter(u -> passwordEncoder.matches(loginRequest.getPassword(), u.getPassword()))
	                .map(u -> {
	                    String token = jwtutil.generateToken(u.getUsername(),u.getRole());
	                    return ResponseEntity.ok(token);
	                })
	                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	    }
	   
}
