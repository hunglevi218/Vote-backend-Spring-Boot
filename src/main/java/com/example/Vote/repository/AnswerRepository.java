package com.example.Vote.repository;

import com.example.Vote.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long questionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Answer e WHERE e.questionId = :questionId")
    void deleteAllByQuestionId(@Param("questionId") Long questionId);

    @Query("SELECT e.id FROM Answer e WHERE e.questionId = :questionId")
    List<Long> findAnswerIdByQuestionId(Long questionId);
}
