package com.github.fashionbrot.config;


import com.github.fashionbrot.exception.AuthException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.OK)
    public String marsException(AuthException e) {
        return e.getMsg();
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object globalException(Exception ex) {
        log.error("exception error:{}",ex);
        return ex.getMessage();
    }


}
