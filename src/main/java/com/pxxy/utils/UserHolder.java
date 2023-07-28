package com.pxxy.utils;

import com.pxxy.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static com.pxxy.constant.SystemConstant.USER_DATA$REMOVE_HANDLERS;

public class UserHolder {

    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();
    private static final Map<UserKey, Object> storage = new ConcurrentHashMap<>();

    public static void handleRemoveUser(UserDTO user) {
        @SuppressWarnings("unchecked")
        List<Consumer<UserDTO>> handlers = (List<Consumer<UserDTO>>) getData(USER_DATA$REMOVE_HANDLERS, user);
        if (handlers != null) {
            handlers.forEach(userDTOConsumer -> userDTOConsumer.accept(user));
        }
        List<UserKey> toBeRemove = new ArrayList<>();
        storage.keySet().forEach(k -> {if (k.user.equals(user)) toBeRemove.add(k);});
        toBeRemove.forEach(storage::remove);
    }

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

    public static void saveUser(UserDTO user) {
        tl.set(user);
    }

    public static UserDTO getUser() {
        return tl.get();
    }

    public static void removeUser() {
        tl.remove();
    }

    public static Object getData(String key) {
        if (key == null) {
            return null;
        }
        UserDTO user = tl.get();
        return getData(key, user);
    }

    public static Object getData(String key, UserDTO user) {
        UserKey k = new UserKey(user, key);
        return storage.get(k);
    }

    public static Object removeData(String key) {
        if (key == null) {
            return null;
        }
        UserDTO user = tl.get();
        UserKey k = new UserKey(user, key);
        return storage.remove(k);
    }

    public static Object putData(String key, Object data) {
        if (key == null) {
            throw new IllegalArgumentException("The key is null. ");
        }
        UserDTO user = tl.get();
        UserKey k = new UserKey(user, key);
        return storage.put(k, data);
    }
}
