package com.pxxy.Enum;

/**
 * @author gxf
 * @date 2023/4/15 21:51
 */
public enum  ExceptionEnum {

    //
    INTERNAL_SERVER_ERROR(500, "服务器异常请联系管理员"),

    DELETED_ERROR(409, "删除失败，请稍后再试"),

    SAVE_ERROR(409, "添加失败，请稍后再试"),

    SELECT_ERROR(409, "查询失败，请稍后再试"),

    UPDATE_ERROR(409, "修改失败，请稍后再试");

    private final Integer code;
    private final String message;

    ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }
}
