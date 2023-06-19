package com.pxxy.exception;

import lombok.Data;

/**
 * @author gxf
 * @date 2023/4/15 21:51
 */
@Data
public class FgwException extends RuntimeException {
    /**
     * 错误状态码
     */
    protected Integer errorCode;
    /**
     * 错误提示
     */
    protected String errorMsg;

    public FgwException(){

    }

    public FgwException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 服务器错误
     * @param errorMsg
     */
    public FgwException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

}
