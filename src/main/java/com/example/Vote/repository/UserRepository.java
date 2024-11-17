package com.example.Vote.repository;

import com.example.Vote.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query("SELECT o.userId FROM User o WHERE o.username = :username")
    Optional<Long> findUserIdByUsername(String username);

}
