package com.github.fashionbrot.util;

import com.github.fashionbrot.annotation.Permission;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.function.*;
import java.lang.reflect.Method;
import java.util.Set;

public class PermissionUtil {



    /**
     * 验证权限
     * {@code
     *  <pre>
     *     获取method方法如下:
     *     private Method getHandlerMethod(Object handler){
     *         if (handler!=null && handler instanceof HandlerMethod) {
     *             HandlerMethod handlerMethod = (HandlerMethod) handler;
     *             Method method = handlerMethod.getMethod();
     *             return method;
     *         }
     *         return null;
     *     }
     * <pre/>
     * }
     * @param method                接口对应方法
     * @param superAdminFunction    获取用户是否是超级管理员
     * @param permissionFunction    获取用户的权限
     * @param annotationFunction    获取method注解的权限
     * @return boolean true代表验证通过 false验证不通过
     */
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


    /**
     * 验证权限
     * {@code
     *  <pre>
     *     获取method方法如下:
     *     private Method getHandlerMethod(Object handler){
     *         if (handler!=null && handler instanceof HandlerMethod) {
     *             HandlerMethod handlerMethod = (HandlerMethod) handler;
     *             Method method = handlerMethod.getMethod();
     *             return method;
     *         }
     *         return null;
     *     }
     * <pre/>
     * }
     * @param method                接口对应方法
     * @param superAdminFunction    获取用户是否是超级管理员
     * @param permissionFunction    获取用户的权限
     * @return boolean true代表验证通过 false验证不通过
     */
    public static boolean checkPermission(Method method,
                                          GetSuperAdminFunction superAdminFunction,
                                          GetPermissionFunction permissionFunction) {

        GetAnnotationFunction annotationFunction = annotationMethod -> {
            if (annotationMethod.getAnnotation(Permission.class) == null) {
                return null;
            }
            return annotationMethod.getAnnotation(Permission.class).value();
        };
        return checkPermission(method,superAdminFunction,permissionFunction,annotationFunction);
    }


    /**
     * 验证权限
     * {@code
     *  <pre>
     *     获取method方法如下:
     *     private Method getHandlerMethod(Object handler){
     *         if (handler!=null && handler instanceof HandlerMethod) {
     *             HandlerMethod handlerMethod = (HandlerMethod) handler;
     *             Method method = handlerMethod.getMethod();
     *             return method;
     *         }
     *         return null;
     *     }
     * <pre/>
     * }
     * @param method                接口对应方法
     * @param permissionFunction    获取用户的权限
     * @return boolean true代表验证通过 false验证不通过
     */
    public static boolean checkPermission(Method method,
                                          GetPermissionFunction permissionFunction) {
        return checkPermission(method,null,permissionFunction);
    }


    /**
     * 验证权限
     * {@code
     *  <pre>
     *     获取method方法如下:
     *     private Method getHandlerMethod(Object handler){
     *         if (handler!=null && handler instanceof HandlerMethod) {
     *             HandlerMethod handlerMethod = (HandlerMethod) handler;
     *             Method method = handlerMethod.getMethod();
     *             return method;
     *         }
     *         return null;
     *     }
     * <pre/>
     * }
     * @param method                接口对应方法
     * @param permissionFunction    获取用户的权限
     * @param annotationFunction    获取method注解的权限
     * @return boolean true代表验证通过 false验证不通过
     */
    public static boolean checkPermission(Method method,
                                          GetPermissionFunction permissionFunction,
                                          GetAnnotationFunction annotationFunction) {
        return checkPermission(method,null,permissionFunction,annotationFunction);
    }



}
