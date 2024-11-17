package com.example.Vote.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerQuestionRequest {
    private Long sessionId;
    private HashMap<Long, Long> quesAnsList;
}