package com.pxxy.utils;

import com.pxxy.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static com.pxxy.constant.SystemConstant.USER_DATA$LOGOUT_HANDLERS;

public class UserHolder {

    /**
     * 本地线程隔离类，用于存储当前线程的用户信息。
     */
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    /**
     * 用于存储用户数据的映射。
     * 用户数据是用户在登录状态下需要用到的缓存数据，每个用户拥有独立一份，互不干扰。
     * @see UserKey
     */
    private static final Map<UserKey, Object> storage = new ConcurrentHashMap<>();

    /**
     * 用户数据映射中的键的类型。
     * 包括一个用户对象和一个用来标识数据内容的字符串。
     */
    private static class UserKey {
        private final UserDTO user;
        private final String key;

        UserKey(UserDTO user, String key) {
            this.user = user;
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserKey userKey1 = (UserKey) o;
            return user.equals(userKey1.user) && key.equals(userKey1.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, key);
        }
    }

    /**
     * 将用户信息保存到当前线程中。
     * @param user 用户信息
     */
    public static void saveUser(UserDTO user) {
        tl.set(user);
    }

    /**
     * 从当前线程中获取用户信息。
     * @return 用户信息
     */
    public static UserDTO getUser() {
        return tl.get();
    }

    /**
     * 从当前线程中移除用户信息，释放资源。
     */
    public static void removeUser() {
        tl.remove();
    }

    /**
     * 获取当前线程的用户的数据，key 是数据标识。
     * @param key 数据标识
     * @return 用户的一项数据
     */
    public static Object getData(String key) {
        if (key == null) {
            return null;
        }
        UserDTO user = tl.get();
        return getData(key, user);
    }

    /**
     * 获取当前线程的用户下线时要执行的相关操作列表。
     * @return 当前线程的用户下线时要执行的相关操作列表
     */
    @SuppressWarnings("unchecked")
    public static List<Consumer<UserDTO>> getLogoutHandlers() {
        List<Consumer<UserDTO>> handlers = (List<Consumer<UserDTO>>) getData(USER_DATA$LOGOUT_HANDLERS);
        if (handlers == null) {
            handlers = new ArrayList<>();
            UserHolder.putData(USER_DATA$LOGOUT_HANDLERS, handlers);
        }
        return handlers;
    }

    /**
     * 执行用户下线时的相关操作。
     * @param user 要执行操作的用户
     */
    public static void handleUserLogout(UserDTO user) {
        @SuppressWarnings("unchecked")
        List<Consumer<UserDTO>> handlers = (List<Consumer<UserDTO>>) getData(USER_DATA$LOGOUT_HANDLERS, user);
        if (handlers != null) {
            handlers.forEach(userDTOConsumer -> userDTOConsumer.accept(user));
        }
        List<UserKey> toBeRemove = new ArrayList<>();
        storage.keySet().forEach(k -> {if (k.user.equals(user)) toBeRemove.add(k);});
        toBeRemove.forEach(storage::remove);
    }

    /**
     * 根据数据标识和用户获取用户的数据。
     * @param key 数据标识
     * @param user 用户
     * @return 用户的数据
     */
    public static Object getData(String key, UserDTO user) {
        UserKey k = new UserKey(user, key);
        return storage.get(k);
    }

    /**
     * 移除当前线程的用户的一项数据。
     * @param key 数据标识
     * @return 移除的数据
     */
    @SuppressWarnings("UnusedReturnValue")
    public static Object removeData(String key) {
        if (key == null) {
            return null;
        }
        UserDTO user = tl.get();
        UserKey k = new UserKey(user, key);
        return storage.remove(k);
    }

    /**
     * 为当前线程的用户添加一项数据。
     * @param key 数据标识
     * @param data 数据内容
     * @return 添加的数据
     */
    public static Object putData(String key, Object data) {
        if (key == null) {
            throw new IllegalArgumentException("The key is null. ");
        }
        UserDTO user = tl.get();
        UserKey k = new UserKey(user, key);
        return storage.put(k, data);
    }
}
