package com.github.fashionbrot.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidTokenException extends RuntimeException {

    private String msg;

    public InvalidTokenException(String msg){
        super(msg);
        this.msg = msg;
    }


}