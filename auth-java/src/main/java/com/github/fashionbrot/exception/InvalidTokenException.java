package com.github.fashionbrot.exception;



public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String msg){
        super(msg);
    }
}