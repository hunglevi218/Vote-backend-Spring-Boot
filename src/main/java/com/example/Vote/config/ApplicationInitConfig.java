package com.example.Vote.config;


import com.example.Vote.entity.Role;
import com.example.Vote.entity.User;
import com.example.Vote.repository.RoleRepository;
import com.example.Vote.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository){
        HashSet<Role> roles = new HashSet<>();
        return args -> {
           if(roleRepository.findById("ADMIN").isEmpty()){
               Role role = Role.builder()
                       .name("ADMIN")
                       .description("admin role")
                       .build();
               roleRepository.save(role);
               roles.add(role);

           }
            if(roleRepository.findById("USER").isEmpty()){
                Role role = Role.builder()
                        .name("USER")
                        .description("user role")
                        .build();
                roleRepository.save(role);
                roles.add(role);
            }
            if(userRepository.findByUsername("ADMIN").isEmpty()){
            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(roles)
                    .build();
            userRepository.save(user);
               log.warn("admin role has created with deafault");

               }
        };
    }
}
