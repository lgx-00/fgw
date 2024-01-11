package com.pxxy.utils;


import cn.hutool.core.util.RandomUtil;
import com.pxxy.entity.dto.UserDTO;
import com.pxxy.entity.vo.OnlineUserVO;
import com.pxxy.service.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TokenUtil {

    private static final Map<Token, UserDTO> TOKEN_MAPPER = new HashMap<>();

    // 令牌编号中允许的字符，生成令牌编号时会从该字符串中获取随机字符序列
    private static final String BASE_STRING = "123456789012345678901234567890" +
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // 令牌的长度
    private static final int LENGTH = 32;

    // 最大存活时间，单位 毫秒
    private static long maxInactiveInterval;

    // 是否保存令牌到文件
    private static boolean storeToken;

    // 保存令牌的文件
    private static String storePath;

    private static UserService userService;

    @Configuration
    public static class ConstInitializr {

        ConstInitializr(
                @Value("${fgw.max-inactive-interval:7200}") long maxInactiveInterval,
                @Value("${fgw.token.store:false}") boolean storeToken,
                @Value("${fgw.token.store-path:tokens}") String storePath,
                @Autowired UserService userService
        ) {
            TokenUtil.maxInactiveInterval = maxInactiveInterval * 1000;
            TokenUtil.userService = userService;
            TokenUtil.storeToken = storeToken;
            TokenUtil.storePath = storePath;
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
        @Getter
        private long deadTime;

        Token(String token) {
            this.token = token;
        }

        static Token generate() {
            updateMapper().forEach(UserHolder::handleUserLogout);
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

        void kill() { this.deadTime = 0; }

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

        @Override
        public String toString() {
            return token + "," + deadTime + ",";
        }
    }

    private static List<UserDTO> updateMapper() {
        List<Token> tokens = new ArrayList<>(TOKEN_MAPPER.size());
        TOKEN_MAPPER.keySet().forEach(token -> {
            if (token.isDead()) {
                tokens.add(token);
            }
        });
        return tokens.stream().map(TOKEN_MAPPER::remove)
                .collect(Collectors.toList());
    }

    private static List<UserDTO> updateMapper(Token token) {
        List<Token> tokens = new ArrayList<>(TOKEN_MAPPER.size());
        TOKEN_MAPPER.keySet().forEach(t -> {
            if (t.isDead()) {
                tokens.add(t);
            }
            if (t.equals(token)) {
                t.refresh();
            }
        });
        return tokens.stream().map(TOKEN_MAPPER::remove)
                .collect(Collectors.toList());
    }

    /**
     * 校验令牌是否正确，并刷新令牌的过期时间
     *
     * @param token 待验证的令牌
     * @return 是否正确
     */
    public static UserDTO getUser(String token) {
        Token t = new Token(token);
        synchronized (TokenUtil.class) {
            updateMapper(t).forEach(UserHolder::handleUserLogout);
            return TOKEN_MAPPER.get(t);
        }
    }

    /**
     * 生成一个与用户相关联的令牌
     *
     * @return 生成的令牌
     */
    public static Token generate(UserDTO userDTO) {
        Token token = Token.generate();
        synchronized (TokenUtil.class) {
            TOKEN_MAPPER.put(token, userDTO);
        }
        return token;
    }

    /**
     * 使一个令牌失效
     *
     * @param token 令牌
     * @return 该令牌对应的用户
     */
    public static UserDTO invalidate(String token) {
        synchronized (TokenUtil.class) {
            UserDTO userDTO = TOKEN_MAPPER.remove(new Token(token));
            if (userDTO != null) {
                UserHolder.handleUserLogout(userDTO);
            }
            return userDTO;
        }
    }

    /**
     * 使一个用户的令牌失效
     *
     * @param user 用户
     */
    public static void invalidate(UserDTO user) {
        synchronized (TokenUtil.class) {
            TOKEN_MAPPER.forEach((k, v) -> {
                if (user.equals(v)) {
                    k.kill();
                }
            });
        }
    }

    /**
     * 使所有令牌失效，并执行用户注销的后置方法
     */
    public static void invalidateAll(Set<Integer> userIds) {
        synchronized (TokenUtil.class) {
            TOKEN_MAPPER.entrySet().stream()
                    .filter(e -> userIds.contains(e.getValue().getUId()))
                    .forEach(e -> e.getKey().kill());
            updateMapper().forEach(UserHolder::handleUserLogout);
        }
    }

    /**
     * 使所有令牌失效，并执行用户注销的后置方法
     */
    public static void invalidateAll() {
        synchronized (TokenUtil.class) {
            TOKEN_MAPPER.keySet().forEach(Token::kill);
            updateMapper().forEach(UserHolder::handleUserLogout);
        }
    }

    public static List<OnlineUserVO> getOnlineUsers() {
        return TOKEN_MAPPER.entrySet().stream().map(OnlineUserVO::new).collect(Collectors.toList());
    }

    /**
     * 执行最终清扫工作，包括下线用户或存储在线用户的令牌。根据静态布尔属性 storeToken 的值来决定是否将令牌信息保存到文件中，若 storeToken 的值为 false，则会强制下线所有用户。该方法一般在关闭服务器时调用。
     */
    public static void cleanup() {
        if (!storeToken) {
            // storeToken 为 false，强制下线所有用户
            invalidateAll();
            return;
        }
        if (storePath == null) {
            log.warn("指定了保存令牌信息却未指定保存令牌信息的文件的地址。使用默认值 \"./tokens\" 作为保存地址。");
            storePath = "tokens";
        }
        storeToken();
    }

    /**
     * 执行载入工作，载入存储的在线用户的令牌。该方法一般在服务器启动完成时调用。
     */
    public static void load() {
        if (storeToken) {
            if (storePath == null) {
                log.warn("指定了恢复令牌却未指定恢复令牌的文件的地址。尝试从默认位置 \"./tokens\" 获取文件。");
                storePath = "tokens";
            }
            restoreToken();
        }
        File file = new File(Optional.ofNullable(storePath).orElse("tokens"));
        if (file.exists() && !file.delete()) {
            log.warn("无法删除旧的令牌信息文件。");
        }
    }

    /**
     * 将令牌信息保存到文件中。保存的文件路径由静态字符串属性 storePath 确定。
     */
    private static void storeToken() {
        File path = new File(storePath).getAbsoluteFile();
        try {
            if (!path.exists() && !((path.getParentFile().exists() || path.getParentFile().mkdirs()) && path.createNewFile())) {
                log.error("无法为保存令牌信息创建文件。");
                return;
            }
        } catch (IOException e) {
            log.error("创建保存令牌信息的文件失败。", e);
            return;
        }
        StringJoiner sj = new StringJoiner(System.lineSeparator());
        TOKEN_MAPPER.forEach((k, v) -> sj.add(k.toString() + v.getUId()));
        String data = sj.toString();
        try(FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("令牌信息写入失败。", e);
        }
    }

    /**
     * 从文件中恢复令牌信息。
     */
    private static void restoreToken() {
        File file = new File(storePath);
        if (!file.exists()) {
            log.warn("无法恢复令牌，因为无法找到保存令牌的文件。");
            return;
        }

        String data;
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buf = new byte[(int) file.length()];
            if (fis.read(buf) != file.length()) {
                log.warn("恢复令牌时读取的数据长度与预计不符。");
            }
            data = new String(buf);
        } catch (IOException e) {
            log.error("无法恢复令牌，因为读取文件时发生异常。", e);
            return;
        }

        String[] lines = data.split(System.lineSeparator());
        Map<Token, Integer> tokenMapUserId = new HashMap<>((int) (lines.length / 0.75 + 1));
        for (String line : lines) {
            String[] s = line.split(",");
            if (s.length != 3) continue;
            String tokenString = s[0];
            long deadTime = Long.parseLong(s[1]);
            Integer userId = Integer.valueOf(s[2]);

            Token token = new Token(tokenString);
            token.deadTime = deadTime;

            tokenMapUserId.put(token, userId);
        }

        Map<Integer, UserDTO> userIdMapUser = userService.getPerms(new HashSet<>(tokenMapUserId.values()));
        tokenMapUserId.forEach((token, userId) -> {
            UserDTO userDTO = userIdMapUser.get(userId);
            if (Objects.isNull(userDTO)) return;
            TOKEN_MAPPER.put(token, userDTO);
        });
        updateMapper().forEach(UserHolder::handleUserLogout);

    }

}
