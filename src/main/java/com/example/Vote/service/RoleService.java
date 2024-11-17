package com.example.Vote.service;

import com.example.Vote.dto.request.RoleCreationRequest;
import com.example.Vote.dto.response.RoleResponse;
import com.example.Vote.entity.Role;
import com.example.Vote.mapper.RoleMapper;
import com.example.Vote.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    public RoleResponse create(RoleCreationRequest request){
        Role role = roleMapper.toRole(request);
        try{
            role = roleRepository.save(role);
        }catch (RuntimeException ex){}

        return roleMapper.toRoleResponse(role);
    }
    public List<RoleResponse> getALl(){
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Transactional
    public void deleteRole(String name){
        roleRepository.deleteById(name);
    }
}
