package com.github.fashionbrot.algorithms;

import com.github.fashionbrot.AuthEncoder;
import com.github.fashionbrot.exception.SignatureVerificationException;
import com.github.fashionbrot.exception.TokenExpiredException;
import com.github.fashionbrot.tlv.TLVUtil;
import com.github.fashionbrot.util.Base64Util;
import com.github.fashionbrot.util.DateUtil;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class RSAAlgorithm extends Algorithm{

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    /**
     * 随机生成密钥对
     * @param keySize  keySize 512 1024
     * @return  KeyPair
     */
    public static KeyPair genKeyPair(Integer keySize) {
        return genKeyPair(keySize,null);
    }

    /**
     * 随机生成密钥对
     * @param keySize  keySize 512 1024
     * @param seed seed
     * @return  KeyPair
     */
    public static KeyPair genKeyPair(Integer keySize,byte[] seed) {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(keySize, !isEmpty(seed)? new SecureRandom(seed): new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }

    /**
     * 判断字节数组是否为空。
     *
     * @param bytes 待判断的字节数组
     * @return 如果字节数组为空，则返回true；否则返回false。
     */
    public static boolean isEmpty(final byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }


    public RSAAlgorithm(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }


    /**
     * 秘钥转换
     * @param publicKey publicKey
     * @return  RSAPublicKey
     */
    public static RSAPublicKey convertPublicKey(String publicKey){
        RSAPublicKey pubKey = null;
        if (publicKey!=null &&!"".equals(publicKey)){
            byte[] decoded = Base64Util.decode(publicKey);
            try {
                pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return pubKey;
    }

    /**
     * 秘钥转换
     * @param privateKey    privateKey
     * @return  RSAPrivateKey
     */
    public static RSAPrivateKey convertPrivateKey(String privateKey){
        RSAPrivateKey priKey = null;
        if (privateKey!=null && !"".equals(privateKey)){
            byte[] decoded = Base64Util.decode(privateKey);
            try {
                priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return priKey;
    }

    /**
     * RSA私钥解密
     *
     * @param bytes         bytes
     * @param privateKey    私钥
     * @return byte[]
     */
    public static byte[] decrypt(byte[] bytes, RSAPrivateKey privateKey) {
        if (bytes!=null && bytes.length>0 && privateKey!=null){
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(bytes);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] encrypt(byte[] bytes,RSAPublicKey publicKey){
        if (bytes!=null && bytes.length>0 && publicKey!=null){
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(bytes);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public <T extends AuthEncoder> T decrypt(Class<T> clazz, String token) {
        try {
            byte[] signatureBytes = Base64Util.decode(token);
            byte[] decrypt = decrypt(signatureBytes, privateKey);
            T authEncoder = TLVUtil.deserialize(clazz, decrypt);
            if (authEncoder==null){
                throw new SignatureVerificationException("Signature verification failure");
            }
            Date issuedAt = authEncoder.getIssuedAt();
            Date expiresAt = authEncoder.getExpiresAt();
            if (!DateUtil.isDateBetweenInclusive(new Date(),issuedAt,expiresAt)){
                throw new TokenExpiredException("token expired");
            }
            return authEncoder;
        } catch (IllegalStateException  | IllegalArgumentException e) {
            throw new SignatureVerificationException("Signature verification failure");
        }
    }

    @Override
    public String encrypt(AuthEncoder encoder) {
        byte[] payloadBytes = TLVUtil.serialize(encoder);
        byte[] signatureBytes = encrypt(payloadBytes,publicKey);
        String signature = Base64Util.encoder(signatureBytes);
        return signature;
    }
}
