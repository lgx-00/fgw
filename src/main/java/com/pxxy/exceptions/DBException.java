package com.pxxy.exceptions;

/**
 * 数据库异常类，用于在执行数据库持久化操作时发生异常后向上传递信息。
 *
 * Create time: 2023/7/25 11:04
 *
 * @author xw
 * @version 1.0
 */
public class DBException extends BaseRuntimeException {

    public DBException(String msg) {
        super(msg);
    }

}
