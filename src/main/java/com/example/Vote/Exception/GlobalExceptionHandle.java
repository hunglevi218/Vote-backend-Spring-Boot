package com.example.Vote.Exception;

import com.example.Vote.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErorrCode.UCATEGORIZE_EXCEPTION.code);
        apiResponse.setMessage(ErorrCode.UCATEGORIZE_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        ErorrCode erorrCode = exception.getErorrCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(erorrCode.getCode());
        apiResponse.setMessage(erorrCode.getMessage());
        return ResponseEntity
                .status(erorrCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErorrCode erorrCode = ErorrCode.UNAUTHORIZED;
        return ResponseEntity.status(erorrCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(erorrCode.code)
                        .message(erorrCode.getMessage())
                        .build()
        );
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidationException(MethodArgumentNotValidException exception){
        ApiResponse apiResponse = new ApiResponse();
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErorrCode erorrCode = ErorrCode.KEY_INVALID;
        try{
            erorrCode = ErorrCode.valueOf(enumKey);
        }catch (IllegalArgumentException ex){

        }
        apiResponse.setCode(erorrCode.getCode());
        apiResponse.setMessage(erorrCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
