package com.github.fashionbrot;

import com.github.fashionbrot.common.date.DateUtil;
import com.github.fashionbrot.common.tlv.TLVUtil;
import com.github.fashionbrot.common.util.Base64Util;
import com.github.fashionbrot.common.util.ByteUtil;
import org.junit.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

public class NewTest {
    public static byte[] calculateHMAC(byte[] data, byte[] key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(data);
        }catch (Exception e){
            return null;
        }
    }

    @Test
    public void test1() throws NoSuchAlgorithmException, InvalidKeyException {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setIssuedDate(new Date());
        messageInfo.setExpiresDate(DateUtil.addDays(new Date(),1));
        messageInfo.setUserId(1);
        messageInfo.setMobile("18888888888");

        byte[] serialize = TLVUtil.serialize(messageInfo);
        System.out.println(Arrays.toString(serialize));

        byte[] key = "12345678".getBytes(); // 你的密钥

        byte[] hmac = calculateHMAC(serialize, key);
        System.out.println(Arrays.toString(hmac));


        System.out.print(Base64Util.encodeBase64String(serialize));
        System.out.print(".");
        System.out.println(Base64Util.encodeBase64String(hmac));


    }

}
