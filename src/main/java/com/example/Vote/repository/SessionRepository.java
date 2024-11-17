package com.example.Vote.repository;

import com.example.Vote.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findBySessionId(String sessionId);

    @Query("SELECT o FROM Session o WHERE o.id = :id AND o.status = :status")
    Optional<Session> findByIdWhereStatus(Long id, String status);

    Optional<Void> deleteBySessionId(String sessionId);

    Session findByStatus(String status);
}
