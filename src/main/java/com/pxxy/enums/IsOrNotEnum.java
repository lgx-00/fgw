package com.pxxy.enums;

/**
 * @Author: hesen
 * @Date: 2023-06-26-10:35
 * @Description:
 */
public enum IsOrNotEnum {
    IS(0,"否"),
    NOT(1,"是");

    public final int status;
    public final String statusContent;

    IsOrNotEnum(int status, String statusContent) {
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
