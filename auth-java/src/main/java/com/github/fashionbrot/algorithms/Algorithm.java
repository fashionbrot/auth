package com.github.fashionbrot.algorithms;




import com.github.fashionbrot.AuthEncoder;
import com.github.fashionbrot.exception.InvalidTokenException;
import com.github.fashionbrot.exception.SignatureVerificationException;
import com.github.fashionbrot.exception.TokenExpiredException;


/**
 * The Algorithm class represents an algorithm to be used in the Signing or Verification process of a Token.
 * <p>
 * This class and its subclasses are thread-safe.
 */
public abstract class Algorithm {


    public static Algorithm HMAC256(String secret) throws IllegalArgumentException {
        return new HMACAlgorithm(AlgorithmType.HmacSHA256, secret);
    }

    public static Algorithm HMAC384(String secret) throws IllegalArgumentException {
        return new HMACAlgorithm( AlgorithmType.HmacSHA384, secret);
    }

    public static Algorithm HMAC512(String secret) throws IllegalArgumentException {
        return new HMACAlgorithm(AlgorithmType.HmacSHA512, secret);
    }


    /**
     * Verify the given token using this Algorithm instance.
     *  @param token
     * @throws InvalidTokenException            invalid token
     * @throws SignatureVerificationException   verification fail
     */
    public abstract <T extends AuthEncoder>T verify(Class<T> clazz,String token) throws InvalidTokenException, SignatureVerificationException, TokenExpiredException;


    public abstract String generateToken(AuthEncoder encoder);



}
