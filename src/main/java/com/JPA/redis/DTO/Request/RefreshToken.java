package com.JPA.redis.DTO.Request;


import lombok.Getter;

@Getter
public class RefreshToken {
    private String refreshToken;
    private String username;
}
