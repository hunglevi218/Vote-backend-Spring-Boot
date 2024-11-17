package com.example.Vote.controller;

import com.example.Vote.dto.response.ApiResponse;
import com.example.Vote.entity.Participant;
import com.example.Vote.service.ParticipantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/participant")
public class ParticipantController {

    ParticipantService participantService;

    @PostMapping("/{sessionId}")
    @PreAuthorize("hasRole('USER')")
    ApiResponse<List<Participant>> addParticipant(@PathVariable("sessionId") Long sessionId, @RequestBody List<String> userNameList){
        ApiResponse<List<Participant>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(participantService.create(sessionId, userNameList));
        return apiResponse;
    }

}
