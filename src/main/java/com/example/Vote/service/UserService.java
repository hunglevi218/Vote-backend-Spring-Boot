package com.example.Vote.service;

import com.example.Vote.Exception.AppException;
import com.example.Vote.Exception.ErorrCode;
import com.example.Vote.config.CustomPage;
import com.example.Vote.dto.request.UserCreationRequest;
import com.example.Vote.dto.request.UserUpdateRequest;
import com.example.Vote.dto.response.UserResponse;
import com.example.Vote.entity.Role;
import com.example.Vote.entity.User;
import com.example.Vote.mapper.UserMapper;
import com.example.Vote.repository.RoleRepository;
import com.example.Vote.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
     public UserResponse createUser(UserCreationRequest request){
        if(userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException();
        if(!request.getRoles().contains("USER")){
            request.getRoles().add("USER");
        }
        User user = userMapper.toUSer(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        List<Role> roles = roleRepository.findAllById(request.getRoles());

        user.setRoles(new HashSet<>(roles));
        try {
            user = userRepository.save(user);
        } catch (AppException exception) {
            throw new AppException(ErorrCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(user);
    }


    public CustomPage<User> getUsers(int page){
        Pageable pageable = PageRequest.of(page, 5);
        return new CustomPage<User>(userRepository.findAll(pageable));
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_EXIST));
        return  userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var roles = roleRepository.findAllById(request.getRoles());
        userMapper.updateUser(user, request);
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id){
         userRepository.deleteById(id);
    }

    @Transactional
     public void deleteUsers(){
        userRepository.deleteAll();
    }
}
