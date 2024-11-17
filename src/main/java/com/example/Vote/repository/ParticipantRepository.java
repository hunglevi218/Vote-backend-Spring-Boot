package com.example.Vote.repository;

import com.example.Vote.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("SELECT o.userId FROM Participant o WHERE o.sessionId = :sessionId")
    Optional<List<Long>> findBySessionId(@Param("sessionId") Long sessionId);

    @Query("SELECT o.isVoted FROM Participant o WHERE o.sessionId = :sessionId AND o.userId = :userId")
    boolean findIsVotedBySessionIdAndUserId(Long sessionId, Long userId);

    Optional<Participant> findBySessionIdAndUserId(Long sessionId, Long userId);
}
