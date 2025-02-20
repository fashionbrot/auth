package com.github.fashionbrot.util;

import com.github.fashionbrot.AuthEncoder;
import com.github.fashionbrot.algorithms.Algorithm;
import com.github.fashionbrot.exception.InvalidTokenException;
import com.github.fashionbrot.exception.SignatureVerificationException;
import com.github.fashionbrot.exception.TokenExpiredException;
import com.github.fashionbrot.function.GetTokenFunction;
import com.github.fashionbrot.function.TokenExceptionFunction;



public class AuthUtil {


    public static String encrypt(Algorithm algorithm, AuthEncoder encoder) {
        return algorithm.encrypt(encoder);
    }

    public static <T extends AuthEncoder> T decrypt(Algorithm algorithm,Class<T> clazz,String token)
            throws InvalidTokenException, SignatureVerificationException, TokenExpiredException {
        return algorithm.decrypt(clazz,token);
    }


    public static <T extends AuthEncoder> T decrypt(Algorithm algorithm,
                                                  Class<T> clazz,
                                 GetTokenFunction tokenFunction,
                                 TokenExceptionFunction tokenExpiredFunction
                                 ){
        if (tokenFunction==null){
            return null;
        }
        String token = tokenFunction.getToken();
        if (token==null || token.isEmpty()){
            return null;
        }
        try {
            return decrypt(algorithm, clazz,token);
        }catch (Exception exception){
            if (tokenExpiredFunction!=null){
                tokenExpiredFunction.throwException(exception);
            }
        }
        return null;
    }


}
