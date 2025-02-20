package com.github.fashionbrot;

import com.github.fashionbrot.algorithms.Algorithm;
import com.github.fashionbrot.algorithms.AlgorithmType;
import com.github.fashionbrot.algorithms.HMACAlgorithm;
import com.github.fashionbrot.algorithms.RSAAlgorithm;
import com.github.fashionbrot.util.AuthUtil;
import org.junit.Test;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Date;

public class RSATest {

    @Test
    public void test(){

        Date date=new Date();

        int keySize = 512;
        String seed = "12345678";

        KeyPair keyPair =RSAAlgorithm.genKeyPair(keySize,seed.getBytes());
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        Algorithm algorithm = new RSAAlgorithm(publicKey,privateKey);

        HMAC256Request auth = new HMAC256Request();
        auth.setIssuedAt(date);
        auth.setExpiresAt(addHours(date,1));
        auth.setUserId(10000000000L);
        auth.setMobile("18888888888");


        String token = AuthUtil.encrypt(algorithm, auth);
        System.out.println("token:"+token);
        System.out.println(token.getBytes().length);

        HMAC256Request verify = AuthUtil.decrypt(algorithm,HMAC256Request.class, token);
        System.out.println("result:"+verify.toString());

        //token:SAbhw6uMiDJIBuHmz4qIMkALMTg4ODg4ODg4ODgwAQw.C6TQ1q9-yRI9YLYvUesgsaYzZIW3kvjGxRHtVdPJdxc
        //result:HMAC256Request(userId=12, mobile=18888888888)
    }



    /**
     * 将指定的小时数添加到给定的日期。
     * @param date 要添加小时数的日期
     * @param hoursToAdd 要添加的小时数
     * @return 带有添加小时数后的新 {@code Date} 对象
     */
    public static Date addHours(Date date, int hoursToAdd) {
        return setCalendar(date, Calendar.HOUR_OF_DAY, hoursToAdd);
    }

    /**
     * 设置给定日期的指定日历字段（例如，SECOND，MINUTE，HOUR_OF_DAY）为给定的值。
     * @param date 要修改的日期
     * @param field 要修改的日历字段
     * @param amount 字段的新值
     * @return 带有修改后的日历字段值的新 {@code Date} 对象
     */
    public static Date setCalendar(Date date,int field,int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

}
