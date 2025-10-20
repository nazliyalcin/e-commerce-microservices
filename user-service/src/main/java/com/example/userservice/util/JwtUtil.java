package com.example.userservice.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
		
	@Value("${jwt.secret}")
    private String secretKey;
	@Value("${jwt.expiration:3600000}")
    private long expirationMs;
	    	    
	    public String generateToken(String username,String role) {
	        return Jwts.builder()
	                .setSubject(username)
	                .claim("role", role != null ? role : "USER")
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
	                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
	                .compact();
	    }
	    
}
