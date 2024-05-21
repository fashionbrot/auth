package com.github.fashionbrot.function;

/**
 * 从 HttpServerRequest 中获取 token值
 *
 */
@FunctionalInterface
public interface GetTokenFunction  {

    /**
     * @<code>
     * <pre>
     *  private static GetTokenFunction getGetTokenFunction(HttpServletRequest request) {
     *     String tokenName = "token";
     *     return () -> {
     *         // Try to get the token from the header
     *         String token = request.getHeader(tokenName);
     *         if (ObjectUtil.isNotEmpty(token)) {
     *             return token;
     *         }
     *
     *         // Try to get the token from the request parameters
     *         token = request.getParameter(tokenName);
     *         if (ObjectUtil.isNotEmpty(token)) {
     *             return token;
     *         }
     *
     *         // Try to get the token from the cookies
     *         Cookie[] cookies = request.getCookies();
     *         if (ObjectUtil.isNotEmpty(cookies)) {
     *             for (Cookie cookie : cookies) {
     *                 if (tokenName.equals(cookie.getName())) {
     *                     return cookie.getValue();
     *                 }
     *             }
     *         }
     *
     *         // Token not found in header, parameters, or cookies
     *         return null;
     *     };
     * }
     * </pre>
     * </code>
     * @return token
     */
    String getToken();

}
