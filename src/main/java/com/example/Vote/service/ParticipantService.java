package com.example.Vote.service;


import com.example.Vote.Exception.AppException;
import com.example.Vote.Exception.ErorrCode;
import com.example.Vote.entity.Participant;
import com.example.Vote.entity.Session;
import com.example.Vote.entity.User;
import com.example.Vote.repository.ParticipantRepository;
import com.example.Vote.repository.SessionRepository;
import com.example.Vote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipantService {
    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    UserRepository userRepository;


    @Transactional
    public List<Participant> create(Long sessionId, List<String> userNameList){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(()-> new AppException(ErorrCode.SESSION_NOT_EXIST));

        //check permission
        if(!name.equals(session.getOwner())){
            throw new AppException(ErorrCode.UNAUTHORIZED);
        }
        List<Participant> participantList = new ArrayList<>();
        for(String userName : userNameList){
            Participant participant = new Participant();
            User user = userRepository.findByUsername(userName)
                    .orElseThrow(()-> new AppException(ErorrCode.USER_NOT_EXIST));
            Long userId = user.getUserId();
            participant.setSessionId(sessionId);
            participant.setUserId(userId);
            participantList.add(participant);
            participantRepository.save(participant);
        }
        return participantList;
    }
}
