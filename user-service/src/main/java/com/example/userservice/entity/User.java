package com.example.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
	    private Long id;

	    @Column(name = "username",nullable = false, unique = true)
	    private String username;
	    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	    @Column(name = "password")
	    private String password;
	    @Column(name = "email")
	    private String email;
	    @Column(name = "role")
	    @JsonIgnore
	    private String role;
	    
}
