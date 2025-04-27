package com.JPA.redis.DTO.Request;

import lombok.Getter;

@Getter
public class CreateUserRequest {
	private String username;
	private String password;
}
