package com.github.fashionbrot;

import com.github.fashionbrot.algorithms.Algorithm;
import com.github.fashionbrot.common.date.DateUtil;
import com.github.fashionbrot.exception.AuthException;
import com.github.fashionbrot.exception.InvalidTokenException;
import com.github.fashionbrot.exception.SignatureVerificationException;
import com.github.fashionbrot.exception.TokenExpiredException;
import com.github.fashionbrot.function.GetTokenFunction;
import com.github.fashionbrot.function.TokenExceptionFunction;
import com.github.fashionbrot.util.AuthUtil;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class HMAC256Test {

    private static final String secret = "+f+KCxF5UVl+O1a+sfafasfs/IDlfkadasfDfsuVXbMp5M8bOvqj1VEmGoB7IEn+";

    @Test
    public void test(){

        Date date=new Date();


        HMAC256Request auth = new HMAC256Request();
        auth.setIssuedAt(date);
        auth.setExpiresAt(addHours(date,1));
        auth.setUserId(12L);
        auth.setMobile("18888888888");


        String token = AuthUtil.encryptHMAC256(secret, auth);
        System.out.println(token);
        System.out.println(token.length());

        HMAC256Request verify = AuthUtil.decryptHMAC256(secret,HMAC256Request.class, token);
        System.out.println(verify.toString());
        System.out.println(verify.toString().length());

    }


    @Test
    public void test2(){

        Date date=new Date();


        HMAC256Request auth = new HMAC256Request();
        auth.setIssuedAt(date);
        auth.setExpiresAt(addHours(date,1));
        auth.setUserId(12L);
        auth.setMobile("18888888888");

        String secret = "12345678";

        Algorithm algorithm=Algorithm.HMAC256(secret);

        String token = AuthUtil.encrypt(algorithm, auth);
        System.out.println(token);
        System.out.println(token.length());

        GetTokenFunction getTokenFunction=new GetTokenFunction() {
            @Override
            public String getToken() {
                return token;
            }
        };

        TokenExceptionFunction tokenExceptionFunction=new TokenExceptionFunction() {
            @Override
            public void throwException(Exception exception) {
                if (exception instanceof InvalidTokenException){
                    AuthException.throwMsg("无效的token");
                }else if (exception instanceof SignatureVerificationException){
                    AuthException.throwMsg("token验证失败");
                }else if (exception instanceof TokenExpiredException){
                    AuthException.throwMsg("token已过期");
                }
            }
        };


        HMAC256Request verify = AuthUtil.decrypt(algorithm,HMAC256Request.class,getTokenFunction,tokenExceptionFunction);
        System.out.println(verify.toString());
        System.out.println(verify.toString().length());

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
