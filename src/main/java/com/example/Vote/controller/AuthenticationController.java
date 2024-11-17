package com.example.Vote.controller;

import com.example.Vote.dto.request.AuthenticatuionRequest;
import com.example.Vote.dto.request.IntrospectRequest;
import com.example.Vote.dto.request.LogoutRequest;
import com.example.Vote.dto.request.RefreshTokenRequest;
import com.example.Vote.dto.response.ApiResponse;
import com.example.Vote.dto.response.AuthenticationResponse;
import com.example.Vote.dto.response.IntrospectResponse;
import com.example.Vote.dto.response.RefreshToken;
import com.example.Vote.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticatuionRequest request) throws ParseException, JOSEException {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.authenticate(request));
        return  apiResponse;
    }

    @PostMapping("/authenticateByRefreshToken")
    ApiResponse<AuthenticationResponse> getAcessTokenByRefreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.getAcessTokenByRefreshToken(request));
        return  apiResponse;
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        ApiResponse<IntrospectResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.introspect(request));
        return apiResponse;
    }
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        authenticationService.logout(request);
        apiResponse.setMessage("logout success");
        return apiResponse;
    }

}
