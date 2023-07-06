package com.pxxy.enums;

/**
 * @Author: hesen
 * @Date: 2023-06-26-10:35
 * @Description:
 */
public enum ProjectStatusEnum {
    FAILURE_TO_REPORT(0,"未上报"),
    NORMAL(1,"正常"),
    PENDING_REVIEW(2,"待审核"),
    UNLOCKED(3,"未锁定"),
    TO_BE_SCHEDULED(4,"待调度");

    public final int val;
    public final String name;

    ProjectStatusEnum(int val, String name) {
        this.val = val;
        this.name = name;
    }

}
