package com.github.fashionbrot;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.github.fashionbrot.common.util.Base64Util;
import com.github.fashionbrot.common.util.MapUtil;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.util.JwtUtil;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

public class JwtTest {


    @Test
    public void test1() throws Exception {
        KeyPair keyPair = JwtUtil.generateRSAKeyPair(512);
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();


        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) aPublic, (RSAPrivateKey) aPrivate);
        String token = JwtUtil.encrypt(algorithm, 10, MapUtil.createMap("userId", 1000));
        System.out.println("token:"+token);

        Map<String, Claim> decode = JwtUtil.decode(algorithm, token);
        System.out.println(decode);
    }

    @Test
    public void test2() throws Exception {

        String secret = "12345678";
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JwtUtil.encrypt(algorithm, 10, MapUtil.createMap("userId", 1000));
        System.out.println("token:"+token);

        String abc = ObjectUtil.byteToString(Base64Util.decode(token.split("\\.")[0])) ;
        System.out.println(abc);
        System.out.println(ObjectUtil.byteToString(Base64Util.decode(token.split("\\.")[1])));

        Map<String, Claim> decode = JwtUtil.decode(algorithm, token);
        System.out.println(decode);
    }


    @Test
    public void test3() throws Exception {
        KeyPair keyPair = JwtUtil.generateECKeyPair(256);
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();

        Algorithm algorithm = Algorithm.ECDSA256((ECPublicKey) aPublic, (ECPrivateKey) aPrivate);
        String token = JwtUtil.encrypt(algorithm, 10, MapUtil.createMap("userId", 1000));
        System.out.println("token:"+token);

        Map<String, Claim> decode = JwtUtil.decode(algorithm, token);
        System.out.println(decode);
    }




}
