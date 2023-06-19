package com.pxxy.Enum;

/**
 * @author gxf
 * @date 2023/4/15 21:51
 */
public enum MessageEnum {

    //
    OK(200, "成功"),

    NO_CONTENT(204,"服务器成功处理了请求，但是没有返回任何内容"),

    MESSAGE_NOT_EXIST(308,"不存在该信息"),

    LOGIC_FAILED(400, "逻辑异常"),

    OPERATION_FAILED(400, "操作异常"),

    AUTH_FAILED(401, "登录过期，请重新登录"),

    AUTH_CODE(401, "验证码过期，请重新登录"),

    NO_PERMISSION(401, "无权限"),

    PARAMETER_ERROR(402,"参数错误"),

    AUTH_NONEXISTENT(403, "账号不存在"),

    AUTH_FORMAT_ERR(403, "账号格式错误"),

    MESSAGE_NOT_FOUND(404,"无结果数据"),

    NOT_ACCEPTABLE(406,"下单付款失败"),

    AUTH_PASSWORD_ERR(409, "用户名或密码错误"),

    AUTH_CODE_ERR(409, "验证码错误"),

    EXPIRE_ERR(409, "账号密码已过期"),

    NOT_EXIST_ORDER_ERR(409, "订单不存在"),

    ORDER_STATUS_ERR(409, "订单状态异常"),

    NOT_EXIST_PHONE_ERR(409, "该手机号未注册"),

    NOT_EXIST_PRODUCT_ERR(409, "商品不存在"),

    NOT_EXIST_QUANTITY_ERR(409, "库存不足"),

    AUTH_NAME_TOO_LONG(410, "用户名称过长"),

    AUTH_NAME_SPECIAL_CHAR(410, "用户名称包含特殊字符"),

    AUTH_NAME_EXIST(410, "用户名称已经存在"),

    SOMETHING_EXIST(410,"某物已存在"),

    REALNAME_EXIST(402,"该信息已经存在"),

    AUTH_PHONE_EXIST(410, "手机号已注册"),

    AUTH_CURRENT_USER(413, "无法编辑当前用户"),

    AUTH_CURRENT_USER_DEL(414, "无法删除当前用户"),

    AUTH_PASSWORD_TOSHORT(414, "密码不能少于8位!"),

    AUTH_PASSWORD_HARD(414, "密码需要包括数字，字母，特殊字符!"),

    AUTH_PASSWORD_TOLONG(414, "密码不能多于30位!"),

    FAIL(500, "网络异常");

    private final Integer code;
    private final String message;

    MessageEnum(Integer code, String message) {
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