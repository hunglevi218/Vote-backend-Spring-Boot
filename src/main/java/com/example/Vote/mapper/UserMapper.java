package com.example.Vote.mapper;

import com.example.Vote.dto.request.UserCreationRequest;
import com.example.Vote.dto.request.UserUpdateRequest;
import com.example.Vote.dto.response.UserResponse;
import com.example.Vote.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUSer(UserCreationRequest request);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget  User user, UserUpdateRequest request);
    UserResponse toUserResponse(User user);
}
