package com.pxxy.enums;

/**
 * @Author: hesen
 * @Date: 2023-06-26-10:35
 * @Description:
 */
public enum ProjectStatusEnum {
    NORMAL(0,"正常"),
    FAILURE_TO_REPORT(1,"未上报"),
    PENDING_REVIEW(2,"待审核"),
    UNLOCKED(3,"未锁定"),
    TO_BE_SCHEDULED(4,"待调度");
    public final int status;
    public final String statusContent;

    ProjectStatusEnum(int status, String statusContent) {
        this.status = status;
        this.statusContent = statusContent;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusContent() {
        return statusContent;
    }
}
