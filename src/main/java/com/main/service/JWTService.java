package com.main.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.GeneratedValue;

@Component
public class JWTService {

	private String secretKey = "";

	public JWTService() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");

		SecretKey sk = keyGenerator.generateKey();
		secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

	}

	public String generateToken(String username) {

		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder().claims().add(claims).subject(username).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30)).and().signWith(getKey()).compact();

		// return
		// "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImFyanVuIiwiaWF0IjoxNTE2MjM5MDIyfQ.7zbeoVR6qW4nvlUQQygd1vkwIh-1VNriTrUqSOOo7us";
	}

	private SecretKey getKey() {
		byte[] keyBytes = Base64.getDecoder().decode(secretKey); // Correct Base64 decoding
		return Keys.hmacShaKeyFor(keyBytes); // Generate HMAC SHA key
	}

	public String extractUserName(String token) {
		// extract the username from jwt token
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
}
