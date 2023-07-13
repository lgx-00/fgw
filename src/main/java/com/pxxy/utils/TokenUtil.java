package com.pxxy.utils;


import cn.hutool.core.util.RandomUtil;
import com.pxxy.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.*;

public class TokenUtil {

    private static final Map<Token, UserDTO> TOKEN_MAPPER = new HashMap<>();

    private static final String BASE_STRING = "123456789012345678901234567890" +
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int LENGTH = 32;

    // 最大存活时间，单位 毫秒
    private static long maxInactiveInterval;

    @Configuration
    public static class ConstInitializr {

        ConstInitializr(@Value("${fgw.max-inactive-interval}") long maxInactiveInterval) {
            TokenUtil.maxInactiveInterval = maxInactiveInterval * 1000;
        }

    }

    /**
     * 令牌
     */
    public static class Token {

        /**
         * 编号
         */
        public final String token;

        /**
         * 过期时间
         */
        private long deadTime;

        Token(String token) {
            this.token = token;
        }

        static Token generate() {
            updateMapper();
            Token candidate;
            do {
                candidate = new Token(RandomUtil.randomString(BASE_STRING, LENGTH));
            } while (TOKEN_MAPPER.containsKey(candidate));
            candidate.refresh();

            return candidate;
        }

        void refresh() {
            this.deadTime = System.currentTimeMillis() + maxInactiveInterval;
        }

        boolean isDead() {
            return deadTime < System.currentTimeMillis();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Token token1 = (Token) o;
            return token.equals(token1.token);
        }

        @Override
        public int hashCode() {
            return Objects.hash(token);
        }
    }

    private static void updateMapper() {
        List<Token> tokens = new ArrayList<>(TOKEN_MAPPER.size());
        TOKEN_MAPPER.keySet().forEach(token -> {
            if (token.isDead()) {
                tokens.add(token);
            }
        });
        tokens.forEach(TOKEN_MAPPER::remove);
    }

    private static void updateMapper(Token token) {
        List<Token> tokens = new ArrayList<>(TOKEN_MAPPER.size());
        TOKEN_MAPPER.keySet().forEach(t -> {
            if (t.isDead()) {
                tokens.add(t);
            }
            if (t.equals(token)) {
                t.refresh();
            }
        });
        tokens.forEach(TOKEN_MAPPER::remove);
    }

    /**
     * 校验令牌是否正确，并刷新令牌的过期时间
     *
     * @param token 待验证的令牌
     * @return 是否正确
     */
    public synchronized static UserDTO getUser(String token) {
        Token t = new Token(token);
        updateMapper(t);
        return TOKEN_MAPPER.get(t);
    }

    /**
     * 生成一个与用户相关联的令牌
     *
     * @return 生成的令牌
     */
    public synchronized static Token generate(UserDTO userDTO) {
        Token token = Token.generate();
        TOKEN_MAPPER.put(token, userDTO);
        return token;
    }

    public synchronized static UserDTO invalid(String token) {
        return TOKEN_MAPPER.remove(new Token(token));
    }

}
