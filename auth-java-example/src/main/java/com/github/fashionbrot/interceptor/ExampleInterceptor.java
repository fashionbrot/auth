package com.github.fashionbrot.interceptor;

import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.common.util.SetUtil;
import com.github.fashionbrot.exception.AuthException;
import com.github.fashionbrot.function.*;
import com.github.fashionbrot.service.ExampleService;
import com.github.fashionbrot.util.PermissionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
public class ExampleInterceptor  implements HandlerInterceptor {

    @Resource
    private ExampleService exampleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        GetTokenFunction tokenFunction = ()->{
            String token = request.getHeader("token");
            if (ObjectUtil.isEmpty(token)){
                token = request.getParameter("token");
            }
            return token;
        };


        Integer userId = PermissionUtil.getToken(exampleService.getAlgorithm(), tokenFunction, tokenExpiredFunction, signatureVerificationFunction, "userId", Integer.class);
        if (userId==null){
            return false;
        }
        Method method = getHandlerMethod(handler);
        if (method!=null){

            GetSuperAdminFunction superAdminFunction=()-> {
                return false;
            };
            GetPermissionFunction permissionFunction=()->{
                return SetUtil.newSet("test1");
            };

            boolean b = PermissionUtil.checkPermission(method, superAdminFunction, permissionFunction);
            if (!b){
                AuthException.throwMsg("没有权限");
            }
        }



        return true;
    }

    TokenExpiredFunction tokenExpiredFunction=(exception)->{
        AuthException.throwMsg("token以过期");
    };

    SignatureVerificationFunction signatureVerificationFunction = (exception)->{
        AuthException.throwMsg("验证签名失败");
    };

    private Method getHandlerMethod(Object handler){
        if (handler!=null && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            return method;
        }
        return null;
    }


}
