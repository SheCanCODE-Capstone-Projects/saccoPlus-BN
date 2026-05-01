package com.saccoplus.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {


    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    // Raw bytes directly — no Base64 needed
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

   //    reads expiry date from token
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // checks if token is expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //checks username AND expiry
    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username)
                && !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}