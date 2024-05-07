package com.github.fashionbrot.function;


@FunctionalInterface
public interface TokenExpiredFunction {

    /**
     * token 过期执行
     * @param exception 失败异常
     */
    void throwException(Exception exception);

}
