package com.github.fashionbrot.interceptor;

import com.github.fashionbrot.Token;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.common.util.SetUtil;
import com.github.fashionbrot.exception.AuthException;
import com.github.fashionbrot.exception.InvalidTokenException;
import com.github.fashionbrot.exception.SignatureVerificationException;
import com.github.fashionbrot.exception.TokenExpiredException;
import com.github.fashionbrot.function.*;
import com.github.fashionbrot.service.ExampleService;
import com.github.fashionbrot.util.AuthUtil;
import com.github.fashionbrot.util.PermissionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class ExampleInterceptor  implements HandlerInterceptor {

    @Resource
    private ExampleService exampleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        GetTokenFunction tokenFunction = getGetTokenFunction(request);



        Token token = AuthUtil.decrypt(exampleService.getAlgorithm(),Token.class,tokenFunction,tokenExceptionFunction);
        if (token==null || token.getUserId()==null){
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

    private static GetTokenFunction getGetTokenFunction(HttpServletRequest request) {
        String tokenName= "token";
        GetTokenFunction tokenFunction = ()->{
            String token = request.getHeader(tokenName);
            if (ObjectUtil.isEmpty(token)){
                token = request.getParameter(tokenName);
            }
            if (ObjectUtil.isEmpty(token)) {
                Cookie[] cookies = request.getCookies();
                if (ObjectUtil.isNotEmpty(cookies)) {
                    Optional<Cookie> first = Arrays.stream(cookies).filter(m -> tokenName.equals(m.getName())).findFirst();
                    if (first.isPresent()) {
                        token = first.get().getValue();
                    }
                }
            }
            return token;
        };
        return tokenFunction;
    }

    TokenExceptionFunction tokenExceptionFunction=new TokenExceptionFunction() {
        @Override
        public void throwException(Exception exception) {
            if (exception instanceof InvalidTokenException){
                AuthException.throwMsg("无效的token");
            }else if (exception instanceof SignatureVerificationException){
                AuthException.throwMsg("token验证失败");
            }else if (exception instanceof TokenExpiredException){
                AuthException.throwMsg("token已过期");
            }
        }
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
