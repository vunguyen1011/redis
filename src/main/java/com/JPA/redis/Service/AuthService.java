package com.JPA.redis.Service;


import javax.security.sasl.AuthenticationException;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce.Cluster.Refresh;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import com.JPA.redis.DTO.Request.CreateUserRequest;
import com.JPA.redis.DTO.Request.TokenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private  final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	public TokenResponse signIn(CreateUserRequest request) {
		Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
		String accessToken=jwtService.generateAccessToken(request.getUsername());
		String refreshToken=jwtService.generateRefreshToken(request.getUsername());
		return TokenResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}
}
