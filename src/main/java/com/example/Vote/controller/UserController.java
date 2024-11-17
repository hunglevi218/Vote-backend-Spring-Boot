package com.example.Vote.controller;

import com.example.Vote.config.CustomPage;
import com.example.Vote.dto.request.UserCreationRequest;
import com.example.Vote.dto.request.UserUpdateRequest;
import com.example.Vote.dto.response.ApiResponse;
import com.example.Vote.dto.response.UserResponse;
import com.example.Vote.entity.User;
import com.example.Vote.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<CustomPage<User>> getUsers(@RequestParam(defaultValue = "0") int page){
        ApiResponse<CustomPage<User>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUsers(page));
        return apiResponse;
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getUser(){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUser());
        return apiResponse;
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") Long userId, @RequestBody UserUpdateRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(userId, request));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    void deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    void deleteUsers(){
        userService.deleteUsers();
    }
}
