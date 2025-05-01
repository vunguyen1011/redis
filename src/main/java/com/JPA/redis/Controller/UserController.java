package com.JPA.redis.Controller;

import com.JPA.redis.DTO.Response.ApiResponse;
import com.JPA.redis.DTO.Response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.JPA.redis.DTO.Request.CreateUserRequest;
import com.JPA.redis.Model.User;
import com.JPA.redis.Service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        var createdUser = userService.createUser(request);
       return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User created successfully")
                .result(createdUser)
                .build();
    }
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Users retrieved successfully")
                .result(users)
                .build();
    }
    @GetMapping("/username")
    public ApiResponse<UserResponse> getUserByUsername(@RequestParam String username) {
         var user = userService.getUserResponseByUsername(username);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User retrieved successfully")
                .result(user)
                .build();
    }
    @PutMapping("/{username}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable String username,
            @RequestParam String newPassword
    ) {
        var updated = userService.updateUserResponseByUsername(username, newPassword);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User updated successfully")
                .result(updated)
                .build();
    }

    @DeleteMapping("/{username}")
    public ApiResponse<Void> deleteUser(@PathVariable String username) {
        userService.deleteUserResponseByUsername(username);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("User deleted successfully")
                .build();
    }

}