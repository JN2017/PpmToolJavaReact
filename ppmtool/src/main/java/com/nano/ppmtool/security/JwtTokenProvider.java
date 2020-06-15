package com.nano.ppmtool.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.nano.ppmtool.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import static com.nano.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static com.nano.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {
	
	//Generate the token
	public String generateToken(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		Date now= new Date(System.currentTimeMillis());
		Date expiryDate = new Date(now.getTime()+EXPIRATION_TIME);
		
		String userId = Long.toString(user.getId());
		
		Map<String, Object> claims = new HashMap<>();
			claims.put("id", Long.toString(user.getId()));
			claims.put("username", user.getUsername());
			claims.put("fullName", user.getFullName());
			
			//token concatenation and build into a string
			return Jwts.builder()
					.setSubject(userId)
					.setClaims(claims)
					.setIssuedAt(now)
					.setExpiration(expiryDate)
					.signWith(SignatureAlgorithm.HS512 , SECRET)
					.compact();
		
	}
	
	//Validate token
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			return true;
		} catch (SignatureException ex) {
			System.out.println("Invalid JWT Signature");
		}catch (MalformedJwtException ex) {
			System.out.println("Invalid JWT token");
		}catch (ExpiredJwtException ex) {
			System.out.println("Expired JWT token");
		}catch (UnsupportedJwtException ex) {
			System.out.println("UnSupported JWT token");
		}catch (IllegalArgumentException ex) {
			System.out.println("JWT claims string is empty");
		}
		return false;
	}
	
	//Get UserId from token
	public Long getUserIdFromJWT(String token) {
		Claims claims= Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		//Long id = Long.parseLong((String) claims.get("id"));
		String id = (String)claims.get("id");
		return Long.parseLong(id);
	}
		
}
