package com.pxxy.utils;


import cn.hutool.core.util.RandomUtil;

import java.util.HashMap;
import java.util.Map;

public class RandomTokenUtil {

    private static final Map<String, Long> EXISTED_TOKEN = new HashMap<>();

    private static final String BASE_STRING = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int LENGTH = 32;

    /**
     * 校验 token 是否正确，并刷新 token 的过期时间
     * @param token 待验证的 token
     * @param maxInactiveInterval 最大存活时间，单位 秒
     * @return 是否正确
     */
    public static boolean verifyAndRefresh(String token, int maxInactiveInterval) {
        long now = System.currentTimeMillis();
        if (EXISTED_TOKEN.getOrDefault(token, 0L) > now) {
            EXISTED_TOKEN.put(token, now + (long) maxInactiveInterval * 1000);
            return true;
        }
        EXISTED_TOKEN.remove(token);
        return false;
    }

    /**
     * 使用生成随机字符串的方式生成一个 token
     * @param maxInactiveInterval 最大存活时间，单位 秒
     * @return token
     */
    public static String generate(int maxInactiveInterval) {

        long now = System.currentTimeMillis();
        EXISTED_TOKEN.forEach((key, value) -> {
            if (value < now) EXISTED_TOKEN.remove(key);
        });

        String tokenCandidate;
        do {
            // 使用生成随机字符串的方式生成一个 Token
            tokenCandidate = RandomUtil.randomString(BASE_STRING, LENGTH);
        } while (EXISTED_TOKEN.containsKey(tokenCandidate));

        long expiredTime = now + (long) maxInactiveInterval * 1000;
        EXISTED_TOKEN.put(tokenCandidate, expiredTime);

        return tokenCandidate;
    }

    public static void invalid(String token) {
        EXISTED_TOKEN.remove(token);
    }

}
