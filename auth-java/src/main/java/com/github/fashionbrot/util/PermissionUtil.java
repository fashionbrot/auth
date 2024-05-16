package com.github.fashionbrot.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.github.fashionbrot.annotation.Permission;
import com.github.fashionbrot.common.util.MethodUtil;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.function.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class PermissionUtil {


    public static Map<String, Claim>  checkToken(Algorithm algorithm,
                                                 GetTokenFunction tokenFunction,
                                                 TokenExceptionFunction tokenExceptionFunction){
        if (tokenFunction==null){
            return null;
        }

        String token = tokenFunction.getToken();
        if (ObjectUtil.isEmpty(token)){
            return null;
        }
        Map<String, Claim> decode = null;
        try {
            decode = JwtUtil.decode(algorithm, token);
        }catch (Exception exception){
            if (tokenExceptionFunction!=null){
                tokenExceptionFunction.throwException(exception);
            }
        }
        return decode;
    }

    /**
     *
     * @param algorithm                     jwt加密算法
     * @param tokenFunction                 获取token函数
     * @param tokenExpiredFunction          验证token 失败异常
     * @param key                           生成token对应key
     * @param resultClass                   返回Class类型 Integer、Long、String、Boolean、Date、Double、Map、List
     * @return                              对应key的值
     * @param <T>                           类型
     */
    public static <T> T getToken(Algorithm algorithm,
                                 GetTokenFunction tokenFunction,
                                 TokenExceptionFunction tokenExpiredFunction,
                                 String key,
                                 Class<T> resultClass){
        Map<String, Claim> stringClaimMap = checkToken(algorithm, tokenFunction, tokenExpiredFunction);
        if (ObjectUtil.isEmpty(stringClaimMap)){
            return null;
        }
        return JwtUtil.get(stringClaimMap, key, resultClass);
    }

    public static <T> T getToken(Algorithm algorithm,
                                 GetTokenFunction tokenFunction,
                                 TokenExceptionFunction tokenExpiredFunction,
                                 Class<T> resultClass){

        Map<String, Claim> stringClaimMap = checkToken(algorithm, tokenFunction, tokenExpiredFunction);

        T t = newInstance(resultClass);
        if (ObjectUtil.isEmpty(stringClaimMap)){
            return t;
        }

        if (t!=null) {
            Field[] declaredFields = t.getClass().getDeclaredFields();
            if (ObjectUtil.isNotEmpty(declaredFields)){
                for (Field declaredField : declaredFields) {
                    if (MethodUtil.isStaticOrFinal(declaredField)){
                        continue;
                    }
                    declaredField.setAccessible(true);
                    String name = declaredField.getName();
                    if (!stringClaimMap.containsKey(name)){
                        continue;
                    }

                    Object value = JwtUtil.get(stringClaimMap, name, declaredField.getType());
                    if (value==null){
                        continue;
                    }

                    try {
                        declaredField.set(t,value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return t;
    }


    public static  <T> T newInstance(Class<T> resultClass){
        try {
            return resultClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }






    public static boolean checkPermission(Method method,
                                          GetSuperAdminFunction superAdminFunction,
                                          GetPermissionFunction permissionFunction,
                                          GetAnnotationFunction annotationFunction){
        if (method==null){
            return false;
        }
        // Check if the user is a super admin
        if (superAdminFunction != null && superAdminFunction.isSuperAdmin()) {
            return true;
        }
        if (annotationFunction==null){
            return false;
        }
        String[] requiredPermissions = annotationFunction.value(method);
        // Check method-level permissions
        if (ObjectUtil.isNotEmpty(requiredPermissions)) {
            Set<String> userPermissions = permissionFunction.getPermission();
            for (String permission : requiredPermissions) {
                if (permission!=null && userPermissions.contains(permission)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean checkPermission(Method method, GetSuperAdminFunction superAdminFunction, GetPermissionFunction permissionFunction) {

        GetAnnotationFunction annotationFunction = annotationMethod -> {
            if (annotationMethod.getAnnotation(Permission.class) == null) {
                return null;
            }
            return annotationMethod.getAnnotation(Permission.class).value();
        };
        return checkPermission(method,superAdminFunction,permissionFunction,annotationFunction);
    }


    public static boolean checkPermission(Method method, GetPermissionFunction permissionFunction) {
        return checkPermission(method,null,permissionFunction);
    }


    public static boolean checkPermission(Method method, GetPermissionFunction permissionFunction,GetAnnotationFunction annotationFunction) {
        return checkPermission(method,null,permissionFunction,annotationFunction);
    }



}
