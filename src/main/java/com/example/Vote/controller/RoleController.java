package com.example.Vote.controller;

import com.example.Vote.dto.request.RoleCreationRequest;
import com.example.Vote.dto.response.ApiResponse;
import com.example.Vote.dto.response.RoleResponse;
import com.example.Vote.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<RoleResponse> create(@RequestBody RoleCreationRequest request){
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(roleService.create(request));
        return apiResponse;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ApiResponse<List<RoleResponse>> getRoles(){
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(roleService.getALl());
        return  apiResponse;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{name}")
    ApiResponse<Void> deleteRole(@PathVariable("name")  String name){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("delete success");
        roleService.deleteRole(name);
        return apiResponse;
    }
}
