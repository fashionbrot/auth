package com.github.fashionbrot.service;


import com.auth0.jwt.algorithms.Algorithm;
import com.github.fashionbrot.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Service
public class ExampleService {

    static Algorithm algorithm;
    static {
//        SecureRandom secureRandom = new SecureRandom();
//        secureRandom.setSeed(123456789L);
//        KeyPair keyPair = JwtUtil.generateKeyPair("RSA",512,secureRandom);
//        algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());

        algorithm = Algorithm.HMAC256("12345678");
    }



    public Algorithm getAlgorithm(){
        return algorithm;
    }



}
