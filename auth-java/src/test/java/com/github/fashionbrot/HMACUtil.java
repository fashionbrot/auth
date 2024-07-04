package com.github.fashionbrot;

import com.github.fashionbrot.common.compress.GzipUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HMACUtil {

    public static byte[] calculateHMAC(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        return mac.doFinal(data);
    }

    public static void main(String[] args) {
        try {
            byte[] data = new byte[]{0x01,0x02,0x03,0x05,0x06,0x07,0x08,0x09,0x10};
            byte[] key = "12345678".getBytes(); // 你的密钥

            byte[] hmac = calculateHMAC(data, key);
            System.out.println(hmac.length);
            System.out.println(Arrays.toString(hmac));
            System.out.println(new String(hmac));

            System.out.println("HMAC-SHA256: " + bytesToHex(hmac));
            System.out.println("HMAC-SHA256: " + bytesToHex(hmac).getBytes().length);

            byte[] byteArray = new byte[]{105, -47, -42, 32, 97, -34, -113, -37, -75, 15, -15, 114, -108, -70, 39, -53, 118, -90, -35, -79, -42, 49, -107, -91, 32, 78, 4, 52, -23, -65, 98, 123};
            String str = new String(byteArray, StandardCharsets.UTF_8);
            System.out.println(str);

            System.out.println(GzipUtil.compress(bytesToHex(hmac)).length);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    // 将 byte[] 转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}

