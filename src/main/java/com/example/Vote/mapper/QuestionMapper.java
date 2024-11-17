package com.example.Vote.mapper;

import com.example.Vote.dto.response.AnswerResponse;
import com.example.Vote.dto.response.QuestionResponse;
import com.example.Vote.entity.Question;

import java.util.List;

public class QuestionMapper {
    public static QuestionResponse mapToQuestionResponse(Question question, List<AnswerResponse> answerResponseList){
        return new QuestionResponse(
                question.getId(),
                question.getContent(),
                answerResponseList
        );
    }
}
