package com.github.fashionbrot.function;

/**
 * 从 HttpServerRequest 中获取 token值
 */
@FunctionalInterface
public interface GetTokenFunction  {

    String getToken();

}
