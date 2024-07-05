## java 权限认证 + token 加密解密

## jwt 加密使用 com.github.fashionbrot.util.AuthUtil
## 权限认证 使用 com.github.fashionbrot.util.PermissionUtil

## AuthUtil 采用tlv序列化数据进行加密解密 使token 长度比jwt生成的token 长度减少一半
### AuthUtil 采用tlv序列化数据进行加密解密token 示例
```java
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
        System.out.println("token:"+token);

        HMAC256Request verify = AuthUtil.decryptHMAC256(secret,HMAC256Request.class, token);
        System.out.println("result:"+verify.toString());

        //token:SAbhw6uMiDJIBuHmz4qIMkALMTg4ODg4ODg4ODgwAQw.C6TQ1q9-yRI9YLYvUesgsaYzZIW3kvjGxRHtVdPJdxc
        //result:HMAC256Request(userId=12, mobile=18888888888)
    }


    @Test
    public void test2(){

        Date date=new Date();


        HMAC256Request auth = new HMAC256Request();
        auth.setIssuedAt(date);
        auth.setExpiresAt(DateUtil.addDays(1));
        auth.setUserId(12L);
        auth.setMobile("18888888888");


        Algorithm algorithm=Algorithm.HMAC256(secret);

        String token = AuthUtil.encrypt(algorithm, auth);
        System.out.println("token:"+token);

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
        System.out.println("result:"+verify.toString());
        //token:SAacyO6ziDJIBpOQ1YqIMkALMTg4ODg4ODg4ODgwAQw.taHftBxsnmuqLOg7LSM4oIByctm7w5AVEPT4y9G9NSs
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


@Data
public class HMAC256Request extends AuthEncoder{

    private Long userId;

    private String mobile;

}

```


### 权限认证示例

```java

@Slf4j
@Component
public class ExampleInterceptor  implements HandlerInterceptor {

    @Resource
    private ExampleService exampleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        GetTokenFunction tokenFunction = getGetTokenFunction(request);



        Token token = AuthUtil.decrypt(exampleService.getAlgorithm(),Token.class,tokenFunction,tokenExceptionFunction);
        if (token==null || token.getUserId()==null){
            return false;
        }
        Method method = getHandlerMethod(handler);
        if (method!=null){

            GetSuperAdminFunction superAdminFunction=()-> {
                return false;
            };
            GetPermissionFunction permissionFunction=()->{
                return SetUtil.newSet("test1");
            };

            boolean b = PermissionUtil.checkPermission(method, superAdminFunction, permissionFunction);
            if (!b){
                AuthException.throwMsg("没有权限");
            }
        }



        return true;
    }

    private static GetTokenFunction getGetTokenFunction(HttpServletRequest request) {
        String tokenName= "token";
        GetTokenFunction tokenFunction = ()->{
            String token = request.getHeader(tokenName);
            if (ObjectUtil.isEmpty(token)){
                token = request.getParameter(tokenName);
            }
            if (ObjectUtil.isEmpty(token)) {
                Cookie[] cookies = request.getCookies();
                if (ObjectUtil.isNotEmpty(cookies)) {
                    Optional<Cookie> first = Arrays.stream(cookies).filter(m -> tokenName.equals(m.getName())).findFirst();
                    if (first.isPresent()) {
                        token = first.get().getValue();
                    }
                }
            }
            return token;
        };
        return tokenFunction;
    }

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

    private Method getHandlerMethod(Object handler){
        if (handler!=null && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            return method;
        }
        return null;
    }


}

```