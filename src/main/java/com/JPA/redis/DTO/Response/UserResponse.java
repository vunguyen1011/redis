package com.JPA.redis.DTO.Response;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserResponse implements Serializable {
    String username;
    String password;
}
