package com.pxxy.utils;


import com.pxxy.constant.ResponseMessage;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: xrw
 **/
@Data
public class ResultResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private T data;

    public ResultResponse() {
    }

    private ResultResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultResponse<?> ok() {
        return new ResultResponse<>(ResponseMessage.OK_CODE, ResponseMessage.OK_MSG, null);
    }

    public static <T> ResultResponse<T> ok(T data) {
        return new ResultResponse<>(ResponseMessage.OK_CODE, ResponseMessage.OK_MSG, data);
    }

    public static <T> ResultResponse<T> ok(String msg, T data) {
        return new ResultResponse<>(ResponseMessage.OK_CODE, msg, data);
    }

    public static <T> ResultResponse<T> fail(String msg) {
        return new ResultResponse<>(ResponseMessage.FAIL_CODE, msg, null);
    }

    public static <T>ResultResponse<T> fail(int errorCode, String msg) {
        return new ResultResponse<>(errorCode, msg, null);
    }

    public static <T> ResultResponse<T> fail(int errorCode, String msg, T data) {
        return new ResultResponse<>(errorCode, msg, data);
    }

}

