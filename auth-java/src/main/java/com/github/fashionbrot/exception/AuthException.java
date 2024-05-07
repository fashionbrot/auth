package com.github.fashionbrot.exception;

import com.github.fashionbrot.function.ThrowMsgFunction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthException extends RuntimeException {

    private int code;
    private String msg;


    public AuthException(String msg){
        super(msg);
        this.code = 1;
        this.msg = msg;
    }

    public AuthException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }



    public static void throwMsg(String msg){
        throw new AuthException(msg);
    }

    public static void throwMsg(int code,String msg){
        throw new AuthException(code,msg);
    }


    public static ThrowMsgFunction isTrue(boolean condition){
        return (code,msg) -> {
            if (condition) {
                AuthException.throwMsg(code,msg);
            }
        };
    }



}
