package com.github.fashionbrot.util;

import com.github.fashionbrot.AuthEncoder;
import com.github.fashionbrot.algorithms.Algorithm;
import com.github.fashionbrot.exception.InvalidTokenException;
import com.github.fashionbrot.exception.SignatureVerificationException;
import com.github.fashionbrot.exception.TokenExpiredException;
import com.github.fashionbrot.function.GetTokenFunction;
import com.github.fashionbrot.function.TokenExceptionFunction;



public class AuthUtil {

    public static String encryptHMAC256(String secret, AuthEncoder encoder){
        return encrypt(Algorithm.HMAC256(secret),encoder);
    }

    public static String encryptHMAC384(String secret,AuthEncoder encoder){
        return encrypt(Algorithm.HMAC384(secret),encoder);
    }

    public static String encryptHMAC512(String secret,AuthEncoder encoder){
        return encrypt(Algorithm.HMAC512(secret),encoder);
    }


    public static <T extends AuthEncoder> T decryptHMAC256(String secret,Class<T> clazz,String token)
            throws InvalidTokenException, SignatureVerificationException, TokenExpiredException{
        return decrypt(Algorithm.HMAC256(secret),clazz,token);
    }

    public static <T extends AuthEncoder> T decryptHMAC384(String secret,Class<T> clazz,String token)
            throws InvalidTokenException, SignatureVerificationException, TokenExpiredException{
        return decrypt(Algorithm.HMAC384(secret),clazz,token);
    }

    public static <T extends AuthEncoder> T decryptHMAC512(String secret,Class<T> clazz,String token)
            throws InvalidTokenException, SignatureVerificationException, TokenExpiredException{
        return decrypt(Algorithm.HMAC512(secret),clazz,token);
    }


    public static String encrypt(Algorithm algorithm, AuthEncoder encoder) {
        return algorithm.generateToken(encoder);
    }

    public static <T extends AuthEncoder> T decrypt(Algorithm algorithm,Class<T> clazz,String token)
            throws InvalidTokenException, SignatureVerificationException, TokenExpiredException {
        return algorithm.verify(clazz,token);
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
