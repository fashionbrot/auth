package com.github.fashionbrot.function;



@FunctionalInterface
public interface ThrowMsgFunction {

    void throwMsg(int code, String msg);

    default void throwMsg(String message) {
        throwMsg(1, message);
    }
}
