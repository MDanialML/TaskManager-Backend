package com.danial.taskmanager.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Utility class for JWT token operations
 * Handles token generation, validation, and extraction
 */

@Component
public class JwtUtil {

    //inject secret key from application.properties
    @Value("${jwt.secret}")
    private String secret;

    //inject expiration time from application.propertise
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Generate JWT token for a user
     * @param username The username to include in token
     * @return JWT token string
     */

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * create JWT token with claims and subject
     * @param claims -  Additional data to include
     * @param subject - The usernaem (subject of the token)
     * @return JWT token string
     */
    private String createToken(Map<String, Object> claims, String subject){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
        .claims(claims) //set custom claims
        .subject(subject) //set username
        .issuedAt(now)
        .expiration(expirationDate)
        .signWith(getSigningKey()) //Sign with secret
        .compact(); //Build the token
    }

    /**
     * Extract username from token
     * @param token - JWT token
     * @return usernmae
     */
    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token
     * @param
     * @return expiration date
     */
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a specific claim from token
     * @param token - JWT token
     * @param claimsResolver - function to extract specific claim
     * @return extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     * @param token - JWT token
     * @return all claims
     */
    private Claims extractAllClaims(String token){
        return Jwts.parser() //Updated: parser() instead of parserBuilder()
                .verifyWith(getSigningKey()) //Updated: verifyWith() instead of setSigningKey()
                .build()
                .parseSignedClaims(token)   //Updated: parseSignedCaims() instead of parseClaimsJws()
                .getPayload();      //Updated: getPayload() instead of getBody
    }

    /**
     * check if token is expired
     * @param token - JWT token
     * @return true if expired, false oterwise
     */
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validate token
     * @param token - JWT token
     * @param username - Username to validate against
     * @return true if valid, false otherwise
     */
    public Boolean validateToken(String token, String username){
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Get signing key from secret
     * @return SecretKey for signing
     */
    private SecretKey getSigningKey(){
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}