package com.ecommerce.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
    SecretKey key = Keys.hmacShaKeyFor(jwtConstant.SECRET_KEY.getBytes());

    public String generateToken(Authentication auth) {
    	String email = ((UserDetails) auth.getPrincipal()).getUsername();
        // Generate JWT token
        String jwt = Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 864000000)) // 10 days
                .claim("email", email) // claim can be anything like email, role, etc.
                .signWith(key)
                .compact();

        return jwt;
    }
    
    public String getEmailFromToken(String jwt) 
    {
    	jwt=jwt.substring(7);
    	Claims claims = Jwts.parser()
                .verifyWith(key) // verify the signature with the key
                .build()
                .parseSignedClaims(jwt) // parse the JWT
                .getPayload(); // get claims/payload
        
        String email=String.valueOf(claims.get("email"));
        
        return email;
    }
}
