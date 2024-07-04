package com.github.fashionbrot.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignatureVerificationException extends RuntimeException {

    private int code;
    private String msg;
    private Object obj;

    public SignatureVerificationException(Object obj) {
        this.obj = obj;
    }

    public SignatureVerificationException(Object obj,Throwable cause) {
        super(cause);
        this.obj = obj;
    }

    public SignatureVerificationException(String msg){
        super(msg);
        this.code = 1;
        this.msg = msg;
    }

    public SignatureVerificationException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }


}
