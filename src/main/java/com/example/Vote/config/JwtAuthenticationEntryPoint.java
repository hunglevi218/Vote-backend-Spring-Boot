package com.example.Vote.config;

import com.example.Vote.Exception.ErorrCode;
import com.example.Vote.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErorrCode erorrCode = ErorrCode.UNAUTHENTICATED;
        response.setStatus(erorrCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(erorrCode.getCode())
                .message(erorrCode.getMessage())
                .build();

        //Chuyển đối tượng thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

        //Đảm bảo ghi phản hồi và gửi nó ra ngoài
        response.flushBuffer();
    }
}
