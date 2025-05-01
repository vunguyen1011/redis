package com.JPA.redis.Mapper;


import com.JPA.redis.DTO.Response.UserResponse;
import com.JPA.redis.Model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
     public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}
