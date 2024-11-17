package com.example.Vote.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErorrCode {
    USER_EXISTED(1001, "user existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002, "username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    KEY_INVALID(1004, "key invalid", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST(1005, "user not exist", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "user unauthenticated", HttpStatus.UNAUTHORIZED),
    TAIKHOAN_NOT_EXIST(1007, "tai khoan not exist", HttpStatus.NOT_FOUND),
    SOTAIKHOAN_EXISTED(1008, "so tai khoan existed", HttpStatus.BAD_REQUEST),
    MA_CIF_EXISTED(1011, "ma CIF existed", HttpStatus.BAD_REQUEST),
    HOSO_NOT_EXIST(1009, "not find ho so", HttpStatus.NOT_FOUND),
    UCATEGORIZE_EXCEPTION(9999, "uncategorize exception", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(1010, "You do not have permission", HttpStatus.FORBIDDEN),
    SESSION_NOT_EXIST(1011, "session not exist", HttpStatus.NOT_FOUND),
    OTHER_SESSION_IS_ACTIVED(1012,"other session is active", HttpStatus.BAD_REQUEST),
    NO_QUESTION_FOR_YOU(1013, "no question for you", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode statusCode;
}
