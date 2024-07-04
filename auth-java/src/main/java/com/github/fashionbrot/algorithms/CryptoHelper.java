package com.github.fashionbrot.algorithms;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptoHelper {

    private static final byte JWT_PART_SEPARATOR = (byte) 46;


    public static byte[] createSignatureFor(String algorithm, byte[] secretBytes, byte[] payloadBytes) throws NoSuchAlgorithmException, InvalidKeyException {
        final Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secretBytes, algorithm));
        return mac.doFinal(payloadBytes);
    }

}
