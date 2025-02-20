package com.github.fashionbrot.util;
import java.util.Base64;

public class Base64Util {

    public static byte[] decode(String str){
        return Base64.getDecoder().decode(str);
    }

    public static String encoder(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }


}
