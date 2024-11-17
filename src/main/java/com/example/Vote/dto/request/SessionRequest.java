package com.example.Vote.dto.request;


import com.example.Vote.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionRequest {
    private String sessionId;
    private String mode;
    private String status;
    private String fromTime;
    private String toTime;
    private List<QuestionRequest> questionList;
}