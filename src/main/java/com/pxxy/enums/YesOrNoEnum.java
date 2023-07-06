package com.pxxy.enums;

/**
 * @Author: hesen
 * @Date: 2023-06-26-10:35
 * @Description:
 */
public enum YesOrNoEnum {
    IS(0,"否"),
    NOT(1,"是");

    public final int val;
    public final String name;

    YesOrNoEnum(int status, String statusContent) {
        this.val = status;
        this.name = statusContent;
    }

}
