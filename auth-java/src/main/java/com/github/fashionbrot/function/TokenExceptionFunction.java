package com.github.fashionbrot.function;


@FunctionalInterface
public interface TokenExceptionFunction {

    /**
     * token 可能发生的异常
     * AlgorithmMismatchException
     * IncorrectClaimException
     * InvalidClaimException
     * JWTCreationException
     * JWTDecodeException
     * JWTVerificationException
     * MissingClaimException
     * SignatureGenerationException
     * SignatureVerificationException
     * TokenExpiredException
     *
     * @param exception 失败异常
     */
    void throwException(Exception exception);

}
