package com.example.seckill.util;

import com.example.seckill.config.RestConfig;
import com.example.seckill.entity.User;
import com.example.seckill.service.UserDeserializer;
import com.example.seckill.service.UserDeserializerFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenManager {


    // 复合结构包含User和最后访问时间
    private static class Token {
        private User user;
        private Date lastAccessTime;
    }

    public static String serverInnerAddress;

    // 本地缓存Map
    private final static Map<String, Token> LOCAL_CACHE = new HashMap<>();

    private TokenManager() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    }


    /**
     * 验证vt的有效性
     *
     * @param vt validate token
     * @return User
     * @throws Exception
     */
    public static User validate(String vt) throws Exception {
        User user = localValidate(vt);

        if (user == null) {
            user = remoteValidate(vt);
        }

        return user;
    }


    /**
     * 远程验证vt的有效性
     *
     * @param vt validate token
     * @return User
     * @throws Exception
     */
    private static User remoteValidate(String vt) throws Exception {

        RestTemplate restTemplate = new RestTemplate(RestConfig.generateHttpRequestFactory());

        byte[] result = restTemplate.getForObject("https://127.0.0.1:8082" + "/validate_service?vt=" + vt, byte[].class);
        // byte[] result = restTemplate.getForObject(serverInnerAddress + "/validate_service?vt=" + vt, byte[].class);

        String ret = new String(result, "utf-8");

        UserDeserializer userDeserializer = UserDeserializerFactory.create();
        User user = userDeserializer.deserail(ret);

        // 处理本地缓存
        cacheUser(vt, user);

        return user;
    }


    /**
     * 本地缓存验证vt有效性
     *
     * @param vt validate token
     * @return User
     */
    private static User localValidate(String vt) {
        // 从缓存中查找数据
        Token token = LOCAL_CACHE.get(vt);

        if (token != null) {    // 用户数据存在
            // 更新最后访问时间
            token.lastAccessTime = new Date();

            // 返回结果
            return token.user;
        }

        return null;
    }


    /**
     * 远程验证成功后将信息写入本地缓存
     *
     * @param vt   validate token
     * @param user User
     */
    private static void cacheUser(String vt, User user) {
        Token token = new Token();
        token.user = user;
        token.lastAccessTime = new Date();
        LOCAL_CACHE.put(vt, token);
    }

    /**
     * @param vt           validate token
     * @param tokenTimeout 过期时间
     * @return Date
     */
    public static Date timeout(String vt, int tokenTimeout) {
        Token token = LOCAL_CACHE.get(vt);

        if (token != null) {    // vt存在
            Date lastAccessTime = token.lastAccessTime;
            //最终过期时间
            Date expires = new Date(lastAccessTime.getTime() + tokenTimeout * 60 * 1000);
            Date now = new Date();

            if (expires.compareTo(now) < 0) {  // 已经过期
                // 从本地缓存中移除
                LOCAL_CACHE.remove(vt);

                // 返回null表示此客户端缓存已经过期
                return null;
            } else {
                return expires;
            }
        } else {     // vt不存在
            return null;
        }
    }


    /**
     * 用户退出时失效相应的缓存
     * @param vt validate token
     */
    public static void invalidate(String vt){
        // 本地缓存移除
        LOCAL_CACHE.remove(vt);
    }


    /**
     * 服务端应用关闭时清空本地缓存, 失效所有信息
     */
    public static void destroy() {
        LOCAL_CACHE.clear();
    }
}
