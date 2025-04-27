package com.JPA.redis.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JPA.redis.DTO.Request.CreateUserRequest;
import com.JPA.redis.DTO.Request.TokenResponse;
import com.JPA.redis.Service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auths")
public class AuthController {
	private final AuthService authService;
	@PostMapping
	TokenResponse signIn(@RequestBody CreateUserRequest request) {
		return authService.signIn(request);
	}
}
