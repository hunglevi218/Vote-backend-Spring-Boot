package com.example.Vote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse {
    private Long id;
    private String sessionId;
    private String mode;
    private String status;
    private String fromTime;
    private String toTime;
    private String owner;
    private List<QuestionResponse> questionList;
}
