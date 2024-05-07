package com.github.fashionbrot.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.fashionbrot.common.util.Base64Util;
import com.github.fashionbrot.common.util.JavaUtil;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final int CALENDAR_SECONDS = Calendar.SECOND;


    /**
     * 生成加密后的JWT。
     *
     * @param sign              加密算法
     * @param expiredSeconds    过期时间（秒）
     * @param payload           载荷信息
     * @return                  加密后的JWT
     *
     * 示例1:RSA
     * <pre>
     *     {@code
     *         KeyPair keyPair = JwtUtil.generateRSAKeyPair(512);
     *         PrivateKey aPrivate = keyPair.getPrivate();
     *         PublicKey aPublic = keyPair.getPublic();
     *
     *         Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) aPublic, (RSAPrivateKey) aPrivate);
     *         String token = JwtUtil.encrypt(algorithm, 10*60, MapUtil.createMap("userId", 1000));
     *         System.out.println("token:"+token);
     *
     *         Map<String, Claim> decode = JwtUtil.decode(algorithm, token);
     *         System.out.println(decode);
     *      }
     * </pre>
     * 示例2:HMAC
     * <pre>
     *     {@code
     *         String secret = "12345678";
     *         Algorithm algorithm = Algorithm.HMAC256(secret);
     *
     *         String token = JwtUtil.encrypt(algorithm, 10*60, MapUtil.createMap("userId", 1000));
     *         System.out.println("token:"+token);
     *
     *         String abc = ObjectUtil.byteToString(Base64Util.decode(token.split("\\.")[0])) ;
     *         System.out.println(abc);
     *         System.out.println(ObjectUtil.byteToString(Base64Util.decode(token.split("\\.")[1])));
     *
     *         Map<String, Claim> decode = JwtUtil.decode(algorithm, token);
     *         System.out.println(decode);
     *     }
     * </pre>
     *
     * 示例3: EC
     * <pre>
     *     {@code
     *         KeyPair keyPair = JwtUtil.generateECKeyPair(256);
     *         PrivateKey aPrivate = keyPair.getPrivate();
     *         PublicKey aPublic = keyPair.getPublic();
     *
     *         Algorithm algorithm = Algorithm.ECDSA256((ECPublicKey) aPublic, (ECPrivateKey) aPrivate);
     *         String token = JwtUtil.encrypt(algorithm, 10*60, MapUtil.createMap("userId", 1000));
     *         System.out.println("token:"+token);
     *
     *         Map<String, Claim> decode = JwtUtil.decode(algorithm, token);
     *         System.out.println(decode);
     *     }
     * </pre>
     *
     */
    public static String encrypt(Algorithm sign,int expiredSeconds,Map<String,Object> payload) {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(CALENDAR_SECONDS, expiredSeconds);
        Date expiresDate = nowTime.getTime();

        return JWT.create()
                // sign time
                .withIssuedAt(iatDate)
                // expire time
                .withExpiresAt(expiresDate)
                .withPayload(payload)
                // signature
                .sign(sign);
    }


    /**
     * 解码JWT令牌。
     *
     * @param sign  加密算法
     * @param token JWT令牌
     * @return 解码后的载荷信息
     */
    public static Map<String, Claim> decode(Algorithm sign,String token) {
        JWTVerifier verifier = JWT.require(sign).build();
        DecodedJWT jwt = verifier.verify(token);
        if (jwt!=null){
            return jwt.getClaims();
        }
        return null;
    }


    /**
     * 生成密钥对。
     *
     * @param algorithm 密钥对生成算法
     * @param keySize   密钥长度
     * @return 生成的密钥对
     */
    public static KeyPair generateKeyPair(String algorithm, int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(keySize); // 设置密钥长度
            return keyPairGenerator.generateKeyPair();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 生成指定长度的RSA密钥对。
     *
     * @param keySize 密钥长度
     * @return 生成的RSA密钥对
     */
    public static KeyPair generateRSAKeyPair(int keySize) {
        return generateKeyPair("RSA",keySize);
    }

    /**
     * 生成指定长度的EC密钥对。
     *
     * @param keySize 密钥长度
     * @return 生成的EC密钥对
     */
    public static KeyPair generateECKeyPair(int keySize) {
        return generateKeyPair("EC",keySize);
    }



    public static  <T> T get(Map<String, Claim> claimMap,String key, Class<T> requiredType) {
        if (claimMap.containsKey(key)) {
            Claim claim = claimMap.get(key);
            if (claim != null) {
                if (requiredType == Long.class) {
                    return requiredType.cast(claim.asLong());
                } else if (requiredType == String.class) {
                    return requiredType.cast(claim.asString());
                } else if (requiredType == Boolean.class) {
                    return requiredType.cast(claim.asBoolean());
                } else if (requiredType == Integer.class) {
                    return requiredType.cast(claim.asInt());
                } else if (requiredType == Double.class){
                    return requiredType.cast(claim.asDouble());
                }else if (requiredType == Date.class) {
                    return requiredType.cast(claim.asDate());
                }else if (JavaUtil.isMap(requiredType)){
                    return (T) claim.asMap();
                }else if (JavaUtil.isArray(requiredType)){
                    return (T) claim.asArray(requiredType.getComponentType());
                }else {
                    return claim.as(requiredType);
                }
            }
        }
        return null;
    }



    /**
     * 将Base64编码的公钥字符串转换为PublicKey对象。
     *
     * @param publicKeyString 要转换的公钥字符串
     * @param algorithm       公钥使用的算法
     * @return 转换后的PublicKey对象
     */
    public static PublicKey convertPublicKey(String publicKeyString,String algorithm) {
        try {
            // 将Base64编码的公钥字符串解码为字节数组
            byte[] publicKeyBytes = Base64Util.decode(publicKeyString);

            // 创建一个X509EncodedKeySpec对象，用于表示公钥的ASN.1编码格式
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

            // 使用RSA算法创建KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

            // 生成公钥对象
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            return publicKey;
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 将Base64编码的私钥字符串转换为PrivateKey对象。
     *
     * @param privateKeyString 要转换的私钥字符串
     * @param algorithm        私钥使用的算法
     * @return 转换后的PrivateKey对象，如果发生错误则返回null
     */
    public static PrivateKey convertPrivateKey(final String privateKeyString,final String algorithm){
        try {
            // 将Base64编码的私钥字符串解码为字节数组
            byte[] privateKeyBytes = Base64Util.decode(privateKeyString);

            // 创建一个PKCS8EncodedKeySpec对象，用于表示私钥的ASN.1编码格式
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

            // 使用RSA算法创建KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

            // 生成私钥对象
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            return privateKey;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 将公钥转换为Base64编码的字符串形式。
     *
     * @param publicKey 要转换的公钥
     * @return 公钥的Base64编码字符串，如果公钥为null，则返回空字符串
     */
    public static String publicKeyToString(final PublicKey publicKey) {
        if (publicKey == null) {
            return "";
        }
        return Base64Util.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 将私钥转换为Base64编码的字符串形式。
     *
     * @param privateKey 要转换的私钥
     * @return 私钥的Base64编码字符串，如果私钥为null，则返回空字符串
     */
    public static String privateKeyToString(final PrivateKey privateKey) {
        if (privateKey == null) {
            return "";
        }
        return Base64Util.encodeBase64String(privateKey.getEncoded());
    }

}
