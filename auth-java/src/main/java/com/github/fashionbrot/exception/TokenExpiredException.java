package com.github.fashionbrot.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TokenExpiredException extends RuntimeException {

    private int code;
    private String msg;
    private Object obj;

    public TokenExpiredException(Object obj) {
        this.obj = obj;
    }

    public TokenExpiredException(Object obj,Throwable cause) {
        super(cause);
        this.obj = obj;
    }

    public TokenExpiredException(String msg){
        super(msg);
        this.code = 1;
        this.msg = msg;
    }

    public TokenExpiredException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }


}