package com.github.fashionbrot.function;


import java.lang.reflect.Method;

/**
 * 获取接口方法注解权限
 */
@FunctionalInterface
public interface GetAnnotationFunction {

    String[] value(Method method);

}
