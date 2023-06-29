package com.pxxy.utils;


import com.pxxy.constant.ResponseConstant;
import com.pxxy.exception.FgwException;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: xrw
 **/
@Data
public class ResultResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private Object data;

    public ResultResponse() {
    }

    private ResultResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultResponse ok() {
        return new ResultResponse(ResponseConstant.OK_CODE, ResponseConstant.OK_MSG, null);
    }

    public static  ResultResponse ok(Object data) {
        return new ResultResponse(ResponseConstant.OK_CODE, ResponseConstant.OK_MSG, data);
    }

    public static  ResultResponse ok(String msg, Object data) {
        return new ResultResponse(ResponseConstant.OK_CODE, msg, data);
    }

    public static  ResultResponse fail(String msg) {
        return new ResultResponse(ResponseConstant.FAIL_CODE, msg, null);
    }

    public static ResultResponse fail(int errorCode, String msg) {
        return new ResultResponse(errorCode, msg, null);
    }

    public static  ResultResponse fail(int errorCode, String msg,Object data) {
        return new ResultResponse(errorCode, msg, data);
    }
    /**
     *自定义异常
     * @param be
     * @return
     */
    public static ResultResponse customException(FgwException be){
        ResultResponse result = new ResultResponse();
        result.setCode(be.getErrorCode());
        result.setMsg(be.getErrorMsg());
        result.setData(null);
        return result;
    }

}

