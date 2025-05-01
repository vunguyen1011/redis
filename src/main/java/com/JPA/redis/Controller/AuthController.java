package com.JPA.redis.Controller;

import com.JPA.redis.DTO.Request.RefreshToken;
import com.JPA.redis.DTO.Response.ApiResponse;
import com.JPA.redis.DTO.Response.TokenResponse;
import com.JPA.redis.Exception.ErrorCode;
import com.JPA.redis.Exception.WebException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import com.JPA.redis.DTO.Request.CreateUserRequest;
import com.JPA.redis.Service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auths")
public class AuthController {
	private final AuthService authService;
	@PostMapping
	ApiResponse<TokenResponse> signIn(@RequestBody CreateUserRequest request) {

		var token= authService.signIn(request);
		return ApiResponse.<TokenResponse>builder()
				.message("Login success")
				.result(token)
				.build();
	}
    @PostMapping("/refresh")
    ApiResponse<TokenResponse> refreshToken(@RequestBody RefreshToken request) {
        var token = authService.refreshToken(request);
        return ApiResponse.<TokenResponse>builder()
                .message("Refresh token success")
                .result(token)
                .build();
    }
	@PostMapping("/logout")
	public ApiResponse<Void> logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new WebException(ErrorCode.TOKEN_CAN_NOT_BE_REFRESHED);
		}

		String token = authHeader.substring(7); // Bỏ "Bearer "
		authService.logout(token);
		return ApiResponse.<Void>builder()
				.message("Logout success")
				.build();
	}

}
