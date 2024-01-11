package com.pxxy.exceptions;

/**
 * Class name: BadRequestException
 * Create time: 2023/7/24 20:09
 *
 * @author xw
 * @version 1.0
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg) {
        super(msg);
    }
}
