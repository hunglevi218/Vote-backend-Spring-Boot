package com.example.Vote.mapper;

import com.example.Vote.dto.response.AnswerResponse;
import com.example.Vote.entity.Answer;

public class AnswerMapper {
    public static AnswerResponse mapToAnswerResponse(Answer answer){
        return new AnswerResponse(
                answer.getId(),
                answer.getDescription()
        );
    }

}
