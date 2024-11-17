package com.example.Vote.mapper;

import com.example.Vote.dto.response.QuestionResponse;
import com.example.Vote.dto.response.SessionResponse;
import com.example.Vote.entity.Session;

import java.util.List;

public class SessionMapper {
    public static SessionResponse mapToSessionResponse(Session session, List<QuestionResponse> questionResponseList){
        return new SessionResponse(
                session.getId(),
                session.getSessionId(),
                session.getMode(),
                session.getStatus(),
                session.getFromTime(),
                session.getToTime(),
                session.getOwner(),
                questionResponseList
        );
    }
}
