package com.github.fashionbrot.function;

/**
 * 验证签名失败调用此方法
 */
@FunctionalInterface
public interface SignatureVerificationFunction {

    /**
     * 验证签名失败 执行
     * @param exception 失败异常
     */
    void throwException(Exception exception);

}
