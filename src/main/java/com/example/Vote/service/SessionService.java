package com.example.Vote.service;

import com.example.Vote.Exception.AppException;
import com.example.Vote.Exception.ErorrCode;
import com.example.Vote.config.CustomPage;
import com.example.Vote.dto.request.*;
import com.example.Vote.dto.response.AnswerResponse;
import com.example.Vote.dto.response.QuestionResponse;
import com.example.Vote.dto.response.SessionResponse;
import com.example.Vote.entity.*;
import com.example.Vote.mapper.AnswerMapper;
import com.example.Vote.mapper.QuestionMapper;
import com.example.Vote.mapper.SessionMapper;
import com.example.Vote.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

@Service
@Data
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;


    public SessionResponse createSession(SessionRequest request){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Session session = new Session();

        session.setOwner(name);
        session.setMode(request.getMode());
        session.setSessionId(request.getSessionId());
        session.setStatus(request.getStatus());
        session.setFromTime(request.getFromTime());
        session.setToTime(request.getToTime());
        session = sessionRepository.save(session);

        if(request.getMode().equals("public")){
            List<User> participantList = userRepository.findAll();
            for(User user : participantList){
                Participant participant = new Participant();
                participant.setUserId(user.getUserId());
                participant.setSessionId(session.getId());
                participantRepository.save(participant);
            }
        }

        List<QuestionRequest> questionRequests = request.getQuestionList();
        List<QuestionResponse> questionResponseList = new ArrayList<>();
        for(QuestionRequest questionRequest : questionRequests){
            Question question = new Question();
            question.setSessionId(session.getId());
            question.setContent(questionRequest.getContent());
            question = questionRepository.save(question);

            List<AnswerRequest> answerRequests = questionRequest.getAnswerList();
            List<AnswerResponse> answerResponseList = new ArrayList<>();
            for (AnswerRequest ans : answerRequests){
                Answer answer = new Answer();
                answer.setQuestionId(question.getId());
                answer.setDescription(ans.getDescription());
                answer = answerRepository.save(answer);
                AnswerResponse answerResponse = AnswerMapper.mapToAnswerResponse(answer);
                answerResponseList.add(answerResponse);
            }
            QuestionResponse questionResponse = QuestionMapper.mapToQuestionResponse(question, answerResponseList);
            questionResponseList.add(questionResponse);

        }
        SessionResponse sessionResponse = SessionMapper.mapToSessionResponse(session, questionResponseList);


        return sessionResponse;
    }

    public List<SessionResponse> getAllSessionList(int page){
        Pageable pageable = PageRequest.of(page, 5);
        CustomPage<Session> sessionList = new CustomPage<>(sessionRepository.findAll(pageable));
        List<SessionResponse> sessionResponseList = new ArrayList<>();
        for (Session session : sessionList.getContent()){
            List<Question> questionList = questionRepository.findAllBySessionId(session.getId());
            List<QuestionResponse> questionResponseList = new ArrayList<>();

            for(Question question : questionList){
                List<Answer> answerList = answerRepository.findAllByQuestionId(question.getId());
                List<AnswerResponse> answerResponseList = new ArrayList<>();
                for(Answer answer : answerList){
                    AnswerResponse answerResponse = AnswerMapper.mapToAnswerResponse(answer);
                    answerResponseList.add(answerResponse);
                }
                QuestionResponse questionResponse = QuestionMapper.mapToQuestionResponse(question, answerResponseList);
                questionResponseList.add(questionResponse);
            }
            SessionResponse sessionResponse = SessionMapper.mapToSessionResponse(session,questionResponseList);
            sessionResponseList.add(sessionResponse);
        }
        return sessionResponseList;
    }

    public SessionResponse getSesssion(String SessionId){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Session session = sessionRepository.findBySessionId(SessionId)
                .orElseThrow(() -> new AppException(ErorrCode.SESSION_NOT_EXIST));
        List<Long> participantList = participantRepository.findBySessionId(session.getId())
                .orElseThrow(()-> new AppException(ErorrCode.NO_QUESTION_FOR_YOU));
        Long userId = userRepository.findUserIdByUsername(name)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_EXIST));
        if(!participantList.contains(userId) || participantRepository.findIsVotedBySessionIdAndUserId(session.getId(), userId)){
            throw new AppException(ErorrCode.NO_QUESTION_FOR_YOU);
        }
        List<Question> questionList = questionRepository.findAllBySessionId(session.getId());
        List<QuestionResponse> questionResponseList = new ArrayList<>();
        for(Question question : questionList){
            List<Answer> answerList = answerRepository.findAllByQuestionId(question.getId());
            List<AnswerResponse> answerResponseList = new ArrayList<>();
            for(Answer answer : answerList){
                AnswerResponse answerResponse = AnswerMapper.mapToAnswerResponse(answer);
                answerResponseList.add(answerResponse);
            }
            QuestionResponse questionResponse = QuestionMapper.mapToQuestionResponse(question, answerResponseList);
            questionResponseList.add(questionResponse);
        }
        SessionResponse sessionResponse = SessionMapper.mapToSessionResponse(session, questionResponseList);
        return sessionResponse;
    }

    public SessionResponse getSesssionActive(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Session session = sessionRepository.findByStatus("active");
        if(session == null) throw new AppException(ErorrCode.SESSION_NOT_EXIST);
        List<Long> participantList = participantRepository.findBySessionId(session.getId())
                .orElseThrow(()-> new AppException(ErorrCode.NO_QUESTION_FOR_YOU));
        Long userId = userRepository.findUserIdByUsername(name)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_EXIST));
        if(!participantList.contains(userId) || participantRepository.findIsVotedBySessionIdAndUserId(session.getId(), userId)){
            throw new AppException(ErorrCode.NO_QUESTION_FOR_YOU);
        }
        List<Question> questionList = questionRepository.findAllBySessionId(session.getId());
        List<QuestionResponse> questionResponseList = new ArrayList<>();
        for(Question question : questionList){
            List<Answer> answerList = answerRepository.findAllByQuestionId(question.getId());
            List<AnswerResponse> answerResponseList = new ArrayList<>();
            for(Answer answer : answerList){
                AnswerResponse answerResponse = AnswerMapper.mapToAnswerResponse(answer);
                answerResponseList.add(answerResponse);
            }
            QuestionResponse questionResponse = QuestionMapper.mapToQuestionResponse(question, answerResponseList);
            questionResponseList.add(questionResponse);
        }
        SessionResponse sessionResponse = SessionMapper.mapToSessionResponse(session, questionResponseList);
        return sessionResponse;
    }

    @Transactional
    public void deleteBySessionId(String sessionId){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Session session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new AppException(ErorrCode.SESSION_NOT_EXIST));
        Long ssId = session.getId();
        if(session.getOwner() != name)
            throw new AppException(ErorrCode.UNAUTHORIZED);
        sessionRepository.deleteById(ssId);
        List<Question> questionList = questionRepository.findAllBySessionId(ssId);
        for(Question question : questionList){
            Long quesId = question.getId();
            answerRepository.deleteAllByQuestionId(quesId);
        }
        questionRepository.deleteAllBySessionId(ssId);
    }

    @Transactional
    public void deleteAll(){
        sessionRepository.deleteAll();
        questionRepository.deleteAll();
        answerRepository.deleteAll();
        participantRepository.deleteAll();
    }

    @Transactional
    public SessionResponse changeSessionStatus(String sessionId, StatusSessionRequest request){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Session session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(()-> new AppException(ErorrCode.SESSION_NOT_EXIST));

        //check permission
        if(!name.equals(session.getOwner())){
            throw new AppException(ErorrCode.UNAUTHORIZED);
        }
        // check có phiên nào đang start chưa
        if(request.getStatus().equals("active")){
            Session activeSession = sessionRepository.findByStatus(request.getStatus());
            if(activeSession != null){
                throw new AppException(ErorrCode.OTHER_SESSION_IS_ACTIVED);
            }
        }
        session.setStatus(request.getStatus());
        session = sessionRepository.save(session);
        List<Question> questionList = questionRepository.findAllBySessionId(session.getId());
        List<QuestionResponse> questionResponseList = new ArrayList<>();
        for(Question question : questionList){
            List<Answer> answerList = answerRepository.findAllByQuestionId(question.getId());
            List<AnswerResponse> answerResponseList = new ArrayList<>();
            for(Answer answer : answerList){
                AnswerResponse answerResponse = AnswerMapper.mapToAnswerResponse(answer);
                answerResponseList.add(answerResponse);
            }
            QuestionResponse questionResponse = QuestionMapper.mapToQuestionResponse(question, answerResponseList);
            questionResponseList.add(questionResponse);
        }
        SessionResponse sessionResponse = SessionMapper.mapToSessionResponse(session, questionResponseList);
        return sessionResponse;
    }

    @Transactional
    public void updateSession(AnswerQuestionRequest request){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Long> participantList = participantRepository.findBySessionId(request.getSessionId())
                .orElseThrow(() -> new AppException(ErorrCode.NO_QUESTION_FOR_YOU));
        Long userId = userRepository.findByUsername(name).get().getUserId();
        if(!participantList.contains(userId) || participantRepository.findIsVotedBySessionIdAndUserId(request.getSessionId(), userId)){
            throw new AppException(ErorrCode.UNAUTHORIZED);
        }
        for(Entry<Long, Long> quesAns : request.getQuesAnsList().entrySet()){
            Answer answer = answerRepository.findById(quesAns.getValue())
                    .orElseThrow(()-> new AppException(ErorrCode.NO_QUESTION_FOR_YOU));
            int voteNumber = answer.getVoteNumber() + 1;
            answer.setVoteNumber(voteNumber);
            answerRepository.save(answer);

        }
        Participant participant = participantRepository.findBySessionIdAndUserId(request.getSessionId(), userId)
                .orElseThrow(()-> new AppException(ErorrCode.UNAUTHORIZED));
        participant.setVoted(true);
        participantRepository.save(participant);
    }

    @Transactional
    public HashMap<Long , HashMap<Long, Integer>> getResult(Long sessionId){
        HashMap<Long , HashMap<Long, Integer>> resultHashMap = new HashMap<>();
        List<Long> questionIdList = questionRepository.findQuestionIdBySessionId(sessionId);
        for(Long questionId : questionIdList){
            HashMap<Long, Integer> answerHashMap = new HashMap<>();
            List<Long> answerIdList = answerRepository.findAnswerIdByQuestionId(questionId);
            for(Long answerId : answerIdList){
                Integer voteNumber = answerRepository.findById(answerId).get().getVoteNumber();
                answerHashMap.put(answerId,voteNumber);
            }
            resultHashMap.put(questionId, answerHashMap);
        }
        return  resultHashMap;
    }
}

