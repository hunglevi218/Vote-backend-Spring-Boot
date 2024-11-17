package com.example.Vote.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException{
    private ErorrCode erorrCode;
    public AppException(ErorrCode erorrCode){
        super();
        this.erorrCode = erorrCode;
    }
}
