package com.github.fashionbrot.algorithms;



import com.github.fashionbrot.AuthEncoder;
import com.github.fashionbrot.common.date.DateUtil;
import com.github.fashionbrot.common.tlv.TLVUtil;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.exception.InvalidTokenException;
import com.github.fashionbrot.exception.SignatureVerificationException;
import com.github.fashionbrot.exception.TokenExpiredException;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;


class HMACAlgorithm extends Algorithm {

    private final byte[] secret;
    private final AlgorithmType algorithmType;

    public HMACAlgorithm(AlgorithmType algorithm,String secretStr) {
        this.secret = getSecretBytes(secretStr);
        this.algorithmType = algorithm;
    }

    //Visible for testing
    static byte[] getSecretBytes(String secret) throws IllegalArgumentException {
        if (secret == null) {
            throw new IllegalArgumentException("The Secret cannot be null");
        }
        return secret.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T extends AuthEncoder> T verify(Class<T> clazz,String token) throws InvalidTokenException, SignatureVerificationException, TokenExpiredException {
        try {
            String[] tokenSplit = token.split("\\.");
            if (ObjectUtil.isEmpty(tokenSplit) || tokenSplit.length!=2){
                throw new InvalidTokenException("Invalid token");
            }
            String payload = tokenSplit[0];
            String signature = tokenSplit[1];

            byte[] payloadBytes = Base64.getUrlDecoder().decode(payload);
            byte[] signatureBytes = Base64.getUrlDecoder().decode(signature);

            boolean valid = MessageDigest.isEqual(sign(payloadBytes),signatureBytes);
            if (!valid) {
                throw new SignatureVerificationException("Signature verification failure");
            }
            T authEncoder = TLVUtil.deserialize(clazz, payloadBytes);
            if (authEncoder==null){
                throw new SignatureVerificationException("Signature verification failure");
            }
            Date issuedAt = authEncoder.getIssuedAt();
            Date expiresAt = authEncoder.getExpiresAt();
            if (!DateUtil.isDateBetweenInclusive(new Date(),issuedAt,expiresAt)){
                throw new InvalidTokenException("token invalid");
            }
            return authEncoder;
        } catch (IllegalStateException  | IllegalArgumentException e) {
            throw new SignatureVerificationException("Signature verification failure");
        }
    }


    @Override
    public String generateToken(AuthEncoder encoder) {
        byte[] payloadBytes = TLVUtil.serialize(encoder);
        byte[] signatureBytes = sign(payloadBytes);


        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payloadBytes);
        String signature = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(signatureBytes);

        return String.format("%s.%s",payload, signature);
    }


    public byte[] sign(byte[] contentBytes)  {
        try {
            return CryptoHelper.createSignatureFor(algorithmType.name(),secret,contentBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
