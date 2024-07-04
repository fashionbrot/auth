package com.github.fashionbrot.service;


import com.github.fashionbrot.algorithms.Algorithm;
import org.springframework.stereotype.Service;


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
