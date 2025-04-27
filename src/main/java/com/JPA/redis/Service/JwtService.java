package com.JPA.redis.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.JPA.redis.Model.User;
import com.JPA.redis.Repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;
    
    @Value("${jwt.private-key}")
    private String privateKey;

    @Value("${jwt.acess-expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String username) {
        return buildToken(username, jwtExpiration);
    }

    public String generateRefreshToken(String username) {
        return buildToken(username, refreshExpiration);
    }

    private String buildToken(String username, long expirationTime) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<String> permissions = user.getUserPermissions().stream()
            .filter(userPermission -> userPermission != null && userPermission.getPermission() != null)
            .map(userPermission -> userPermission.getPermission().getAuthority())
            .collect(Collectors.toList());

        return Jwts.builder()
            .setSubject(username)
            .setIssuer("NguyenVu")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .claim("permissions", permissions)
            .signWith(getKey())
            .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
    private <T> T extractClaim(String token,Function<Claims,T>claimReslover) {
    	Claims claims=extractClaims(token);
    	return claimReslover.apply(claims);
    	
    }
    public List<String>extractPermision(String token){
    	return extractClaim(token, claims -> claims.get("permissions", List.class));
    	
    }
}