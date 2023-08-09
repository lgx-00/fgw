package com.pxxy.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hesen
 * @since 2023-06-26-10:35
 * @Description:
 */
public enum YesOrNoEnum {
    YES(1, "是"),
    NO(0, "否");

    private static final Map<String, YesOrNoEnum> mapper = new HashMap<>(2);

    static {
        for (YesOrNoEnum e : YesOrNoEnum.values()) {
            mapper.put(e.name, e);
        }
    }

    public final int val;
    public final String name;

    YesOrNoEnum(int status, String statusContent) {
        this.val = status;
        this.name = statusContent;
    }

    public static Integer from(String s) {
        if (s == null) return null;
        YesOrNoEnum yesOrNoEnum = mapper.get(s);
        if (yesOrNoEnum == null) {
            throw new IllegalArgumentException(s);
        }
        return yesOrNoEnum.val;
    }

}
