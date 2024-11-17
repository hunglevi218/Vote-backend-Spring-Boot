package com.example.Vote.mapper;

import com.example.Vote.dto.request.RoleCreationRequest;
import com.example.Vote.entity.Role;
import org.mapstruct.Mapper;
import com.example.Vote.dto.response.RoleResponse;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toRole(RoleCreationRequest request);
    RoleResponse toRoleResponse(Role role);
}
