package com.pxxy.exceptions;

/**
 * Class name: BaseRuntimeException
 *
 * Create time: 2023/7/25 11:12
 *
 * @author xw
 * @version 1.0
 */
public class BaseRuntimeException extends RuntimeException {

    public BaseRuntimeException() {super();}

    public BaseRuntimeException(String message) {super(message);}

    public BaseRuntimeException(String msg, Throwable cause) {super(msg, cause);}

    public BaseRuntimeException(Throwable cause) {super(cause);}

}
