package com.example.Vote.repository;

import com.example.Vote.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllBySessionId(Long sessionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Question e WHERE e.sessionId = :sessionId")
    void deleteAllBySessionId(@Param("sessionId") Long sessionId);

    @Query("SELECT e.id FROM Question e WHERE e.sessionId = :sessionId")
    List<Long> findQuestionIdBySessionId(Long sessionId);
}
