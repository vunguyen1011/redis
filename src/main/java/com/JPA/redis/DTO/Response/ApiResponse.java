package com.JPA.redis.DTO.Response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private int code=200;
    private String message;
    private T result;

}
