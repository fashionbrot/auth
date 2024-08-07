package com.github.fashionbrot;

import com.github.fashionbrot.common.util.SetUtil;
import com.github.fashionbrot.exception.AuthException;
import com.github.fashionbrot.util.PermissionUtil;
import org.junit.Test;
import java.lang.reflect.Method;

public class CheckPermissionTest {


    @Test
    public void test1() throws NoSuchMethodException {
        TestClass test=new TestClass();
        Method method = test.getClass().getDeclaredMethod("test", null);

        boolean b = PermissionUtil.checkPermission(method, () -> {
            return false;
        }, () -> {
            return SetUtil.newSet("test1");
        });

        AuthException.isTrue(!b).throwMsg("没有权限");

        System.out.println(b);
    }

    @Test
    public void test2() throws NoSuchMethodException {
        TestClass test = new TestClass();
        Method method = test.getClass().getDeclaredMethod("test", null);

        boolean b = PermissionUtil.checkPermission(method, () -> {
            return false;
        }, () -> {
            return SetUtil.newSet("test1");
        }, annotationMethod -> {
            if (annotationMethod.getAnnotation(TestPermission.class) == null) {
                return null;
            }
            return annotationMethod.getAnnotation(TestPermission.class).value();
        });

        AuthException.isTrue(!b).throwMsg("没有权限");

        System.out.println(b);
    }

//
//    @Test
//    public void test3() throws Exception {
//
//        String secret = "12345678";
//        Algorithm algorithm = Algorithm.HMAC256(secret);
//
//        String token = JwtUtil.encrypt(algorithm, 10, MapUtil.createMap("userId", 1000));
//        System.out.println("token:"+token);
//
//        GetTokenFunction tokenFunction = ()-> {
//            return token;
//        };
//
//        Map<String, Claim> stringClaimMap = JwtUtil.checkToken(algorithm, tokenFunction,tokenExpiredFunction);
//        System.out.println(stringClaimMap);
//    }
//
//
//    @Test
//    public void test4() throws Exception {
//
//        String secret = "12345678";
//        Algorithm algorithm = Algorithm.HMAC256(secret);
//
//        String[] array = new String[]{"1"};
//        List<Integer> list=new ArrayList<>();
//        list.add(1);
//
//        Map<String, Object> mm=new HashMap<>();
//        mm.put("abc",22);
//
//
//        Map<String, Object> map = MapUtil.createMap("userId", 1000, "array", array, "list", list,"str","张三","date",new Date(),"map",mm);
//
//        String token = JwtUtil.encrypt(algorithm, 10,map);
//        System.out.println("token:"+token);
//
//        GetTokenFunction tokenFunction = ()-> {
//            return token;
//        };
//
//        TokenModel tokenModel = JwtUtil.getToken(algorithm, tokenFunction,tokenExpiredFunction,TokenModel.class);
//        System.out.println(tokenModel);
//    }
//
//    TokenExceptionFunction tokenExpiredFunction = (exception)->{
//        AuthException.throwMsg("token 验证失败");
//    };
//
//
//
//
//    @Test
//    public void test5() throws Exception {
//
//        String secret = "12345678";
//        Algorithm algorithm = Algorithm.HMAC256(secret);
//
//        String[] array = new String[]{"1"};
//        List<Integer> list=new ArrayList<>();
//        list.add(1);
//
//        Map<String, Object> mm=new HashMap<>();
//        mm.put("abc",22);
//
//
//        Map<String, Object> map = MapUtil.createMap("userId", 1000, "array", array, "list", list,"str","张三","date",new Date(),"map",mm);
//
//        String token = JwtUtil.encrypt(algorithm, 10,map);
//        System.out.println("token:"+token);
//
//        GetTokenFunction tokenFunction = () -> {
//            return token;
//        };
//
//        ArrayList userId = JwtUtil.getToken(algorithm, tokenFunction,tokenExpiredFunction,"list",ArrayList.class);
//        System.out.println(userId);
//    }

}
