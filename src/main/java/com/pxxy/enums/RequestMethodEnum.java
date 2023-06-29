package com.pxxy.enums;

/**
 * @Author: hesen
 * @Date: 2023-06-21-16:38
 * @Description:
 */
public enum RequestMethodEnum {

    GET(3, "你没有查询权限"),
    POST(2, "你没有增加权限"),
    PUT(1, "你没有修改权限"),
    DELETE(0, "你没有删除权限");

    public final int digit;
    public final String msg;

    RequestMethodEnum(int i, String msg) {
        this.digit = i;
        this.msg = msg;
    }

}
