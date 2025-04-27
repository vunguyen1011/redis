package com.JPA.redis.DTO.Request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
	private String accessToken;
	private String refreshToken;
}
