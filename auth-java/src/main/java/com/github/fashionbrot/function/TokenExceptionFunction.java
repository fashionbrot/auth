package com.github.fashionbrot.function;


@FunctionalInterface
public interface TokenExceptionFunction {

    /**
     * token 可能发生的异常
     * InvalidTokenException
     * SignatureVerificationException
     * TokenExpiredException
     *
     * @param exception 失败异常
     */
    void throwException(Exception exception);

}
