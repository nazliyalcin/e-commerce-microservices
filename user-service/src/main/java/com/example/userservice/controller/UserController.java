package com.example.userservice.controller;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;

import java.util.List;

import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/users")
public class UserController {
	    private final UserRepository userRepository;

	    public UserController(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }

	    @GetMapping
	    public List<User> allUsers() {
	        return userRepository.findAll();
	    }

	    @PostMapping
	    public User createUser(@RequestBody User user) {
	    	
	       System.out.println("Gelen JSON -> " + user.getUsername()+ ", " + user.getPassword() + ", " + user.getEmail());

	        return userRepository.save(user);
	    }
}
