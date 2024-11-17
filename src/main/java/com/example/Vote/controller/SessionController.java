package com.example.Vote.controller;

import com.example.Vote.dto.request.AnswerQuestionRequest;
import com.example.Vote.dto.request.SessionRequest;
import com.example.Vote.dto.request.StatusSessionRequest;
import com.example.Vote.dto.response.ApiResponse;
import com.example.Vote.dto.response.SessionResponse;
import com.example.Vote.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<SessionResponse> creatSession(@RequestBody SessionRequest request){
        ApiResponse<SessionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(sessionService.createSession(request));
        return apiResponse;
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<SessionResponse>> getAllSessionList(@RequestParam(defaultValue = "0") int page){
        ApiResponse<List<SessionResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(sessionService.getAllSessionList(page));
        return apiResponse;
    }

    @GetMapping("/{sessionId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<SessionResponse> getSession(@PathVariable("sessionId") String sessionId){
        ApiResponse<SessionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(sessionService.getSesssion(sessionId));
        return apiResponse;
    }
    @GetMapping("/active")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<SessionResponse> getSessionActive(){
        ApiResponse<SessionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(sessionService.getSesssionActive());
        return apiResponse;
    }

    @DeleteMapping("/{sessionId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<Void> deleteSession(@PathVariable("sessionId") String sessionId){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        sessionService.deleteBySessionId(sessionId);
        apiResponse.setMessage("Delete Success !!");
        return apiResponse;
    }
    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<Void> deleteAll(){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        sessionService.deleteAll();
        apiResponse.setMessage("Delete Success !!");
        return apiResponse;
    }

    @PutMapping("/active/{sessionId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<SessionResponse> changeSessionStatus(@PathVariable("sessionId") String sessionId, @RequestBody StatusSessionRequest request){
        ApiResponse<SessionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(sessionService.changeSessionStatus(sessionId, request));
        return apiResponse;
    }

    @PutMapping("/{sessionId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<Void> updateSession(@RequestBody AnswerQuestionRequest request){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        sessionService.updateSession(request);
        return apiResponse;
    }

    @GetMapping("/result/{sessionId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<HashMap<Long , HashMap<Long, Integer>>> getResult(@PathVariable("sessionId") Long sessionId){
        ApiResponse<HashMap<Long , HashMap<Long, Integer>>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(sessionService.getResult(sessionId));
        return apiResponse;
    }
}
