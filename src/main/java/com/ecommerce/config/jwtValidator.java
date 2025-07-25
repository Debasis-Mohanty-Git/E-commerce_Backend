package com.ecommerce.config;

import java.io.IOException;
import java.security.KeyStore.SecretKeyEntry;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class jwtValidator extends OncePerRequestFilter
{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt=request.getHeader(jwtConstant.JWT_Header);
		if(jwt!=null) {
			//Bearere token sothat we use 7
			jwt =jwt.substring(7);
			
			try 
			{
			SecretKey key=Keys.hmacShaKeyFor(jwtConstant.SECRET_KEY.getBytes());
			  
		            // Parse and validate the JWT
		            Claims claims = Jwts.parser()
		                    .verifyWith(key) // verify the signature with the key
		                    .build()
		                    .parseSignedClaims(jwt) // parse the JWT
		                    .getPayload(); // get claims/payload
		            
		            String email=String.valueOf(claims.get("email"));
		            String authorities=String.valueOf("authorities");
		            List<GrantedAuthority> auths=AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
		            Authentication authentication=new UsernamePasswordAuthenticationToken(email, null,auths);
		            SecurityContextHolder.getContext().setAuthentication(authentication);
		            }
			catch(Exception e) {
				throw new BadCredentialsException("Invalid token... from jwt validator");
				
			}
		}
		filterChain.doFilter(request, response);
	}
}
